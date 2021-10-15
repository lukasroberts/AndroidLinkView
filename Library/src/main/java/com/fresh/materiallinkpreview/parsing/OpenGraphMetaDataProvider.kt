package com.fresh.materiallinkpreview.parsing

import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.models.OpenGraphNamespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

class OpenGraphMetaDataProvider : IOpenGraphMetaDataProvider {
    private val pageNamespaces = mutableListOf<OpenGraphNamespace>()

    override suspend fun startFetchingMetadataAsync(link: URL): Result<OpenGraphMetaData> {
        return withContext(Dispatchers.IO) {
             val metaData = startFetchingMetadata(link)

            if(metaData != null) {
                return@withContext Result.success(metaData)
            } else {
                return@withContext Result.failure(Exception())
            }
        }
    }

    override fun startFetchingMetadata(link: URL): OpenGraphMetaData? {
        // first only, get the head element of the webpage, as we do not want to download the entire thing!
        val httpUrlConnection = link.openConnection() as HttpURLConnection

        val charset = getConnectionCharset(httpUrlConnection)
        val bufferedInput = BufferedReader(InputStreamReader(httpUrlConnection.inputStream, charset))
        val bufferedHeadContents = StringBuffer()

        while (true) {
            var inputLine = bufferedInput.readLine() ?: break

            if (inputLine.contains(CLOSING_HEAD_STRING)) {
                val indexOfClose = inputLine.indexOf(CLOSING_HEAD_STRING) + CLOSING_HEAD_STRING.length
                inputLine = inputLine.substring(0, indexOfClose)
                inputLine = inputLine.plus("<body></body></html>")
                bufferedHeadContents.append(inputLine + "\r\n")
                break
            }

            bufferedHeadContents.append(inputLine + "\r\n")
        }

        val completeHeadContents = bufferedHeadContents.toString()
        val document = Jsoup.parse(completeHeadContents)

        var hasOpenGraphSpecification = false

        // try and identify any namespace information if it exists
        if(document.head().hasAttr("prefix")) {
            val prefixElements = document.head().getElementsByAttribute("prefix").html()
            val pattern = Pattern.compile("(([A-Za-z0-9_]+):\\s+(http://ogp.me/ns(/\\w+)*#))\\s*")
            val matcher = pattern.matcher(prefixElements)

            while (matcher.find()) {
                val prefix = matcher.group(2)
                val documentUri = matcher.group(3)

                if(!prefix.isNullOrEmpty() && !documentUri.isNullOrEmpty()) {
                    pageNamespaces.add(OpenGraphNamespace(prefix, documentUri))
                }

                hasOpenGraphSpecification = !prefix.isNullOrEmpty() && prefix.equals("og")
            }
        }

        // if no namespace information exists, add some defaults
        if(!hasOpenGraphSpecification) {
            pageNamespaces.add(OpenGraphNamespace("og", "https://ogp.me/ns#"))
        }

        val metaElements = document.head().getElementsByTag("meta")
        val openGraphMetaData = OpenGraphMetaData()

        // go through every meta element that we have collected, and try to identify any open graph tags
        // once we have identified them, sort them appropriately into the data structure
        for (metaElement in metaElements) {
            for (namespace in pageNamespaces) {
                var target = ""

                if(metaElement.hasAttr("property")) {
                    target = "property"
                } else if(metaElement.hasAttr("name")) {
                    target = "name"
                }

                if(target.isNotEmpty()
                    && metaElement.attr(target).startsWith(namespace.prefix + ":")) {

                    when(metaElement.attr(target)) {
                        "og:title" -> openGraphMetaData.title = metaElement.attr("content")
                        "og:type" -> openGraphMetaData.type = metaElement.attr("content")
                        "og:image" -> openGraphMetaData.imageUrl = metaElement.attr("content")
                        "og:url" -> openGraphMetaData.url = metaElement.attr("content")
                        "og:description" -> openGraphMetaData.description = metaElement.attr("content")
                    }

                    break
                }
            }
        }

        return openGraphMetaData
    }

    /**
     * Original credit to https://github.com/johndeverall/opengraph-java/blob/master/src/main/java/org/opengraph/OpenGraph.java
     *
     * Gets the charset for specified connection.
     * Content Type header is parsed to get the charset name.
     *
     * @param connection the connection.
     * @return the Charset object for response charset name;
     *         if it's not found then the default charset.
     */
    private fun getConnectionCharset(connection: HttpURLConnection): Charset? {
        var contentType: String = connection.contentType
        if (contentType.isNotEmpty()) {
            contentType = contentType.lowercase()
            val charsetName = extractCharsetName(contentType)
            if (!charsetName.isNullOrEmpty()) {
                try {
                    return Charset.forName(charsetName)
                } catch (e: Exception) {
                    // specified charset is not found,
                    // skip it to return the default one
                }
            }
        }

        // return the default charset
        return Charset.defaultCharset()
    }

    /**
     * Original credit to https://github.com/johndeverall/opengraph-java/blob/master/src/main/java/org/opengraph/OpenGraph.java
     *
     * Extract the charset name form the content type string.
     * Content type string is received from Content-Type header.
     *
     * @param contentType the content type string, must be not null.
     * @return the found charset name or null if not found.
     */
    private fun extractCharsetName(contentType: String): String? {
        // split onto media types
        val mediaTypes = contentType.split(":").toTypedArray()
        if (mediaTypes.isNotEmpty()) {
            // use only the first one, and split it on parameters
            val params = mediaTypes[0].split(";").toTypedArray()

            // find the charset parameter and return it's value
            for (each in params) {
                val scoped = each.trim { it <= ' ' }
                if (scoped.startsWith("charset=")) {
                    // return the charset name
                    return scoped.substring(8).trim { it <= ' ' }
                }
            }
        }

        return null
    }

    companion object {
        private const val CLOSING_HEAD_STRING = "</head>"
    }
}