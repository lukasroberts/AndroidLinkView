package com.fresh.materiallinkpreview.parsing

import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.models.OpenGraphNamespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.regex.Pattern

class OpenGraphMetaDataProvider : IOpenGraphMetaDataProvider {
    private val pageNamespaces = mutableListOf<OpenGraphNamespace>()

    override suspend fun startFetchingMetadataAsync(link: URL): Result<OpenGraphMetaData> {
        return withContext(Dispatchers.IO) {
            try {
                val metaData = startFetchingMetadata(link)
                return@withContext Result.success(metaData)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override fun startFetchingMetadata(link: URL): OpenGraphMetaData {
        if (link.isImage()) {
            return OpenGraphMetaData("", link.toString(), link.toString(), "website")
        }

        // first only, get the head element of the webpage, as we do not want to download the entire thing!
        var httpUrlConnection = link.openConnection() as HttpURLConnection

        if (link.protocol == "http") {
            // check for any potential re-directs and perform them
            var redirect = false

            // normally, 3xx is redirect
            if (httpUrlConnection.responseCode != HttpURLConnection.HTTP_OK) {
                if (httpUrlConnection.responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || httpUrlConnection.responseCode == HttpURLConnection.HTTP_MOVED_PERM
                    || httpUrlConnection.responseCode == HttpURLConnection.HTTP_SEE_OTHER
                ) {
                    redirect = true
                }
            }

            if (redirect) {
                val redirectUrl = httpUrlConnection.getHeaderField("Location")

                if (!redirectUrl.isNullOrEmpty()) {
                    httpUrlConnection = URL(redirectUrl).openConnection() as HttpURLConnection
                }
            }
        }

        val charset = getConnectionCharset(httpUrlConnection)
        val bufferedInput =
            BufferedReader(InputStreamReader(httpUrlConnection.inputStream, charset))
        val bufferedHeadContents = StringBuffer()

        while (true) {
            var inputLine = bufferedInput.readLine() ?: break

            if (inputLine.contains(CLOSING_HEAD_STRING)) {
                val indexOfClose =
                    inputLine.indexOf(CLOSING_HEAD_STRING) + CLOSING_HEAD_STRING.length
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
        if (document.head().hasAttr("prefix")) {
            val prefixElements = document.head().getElementsByAttribute("prefix").html()
            val pattern = Pattern.compile("(([A-Za-z0-9_]+):\\s+(https://ogp.me/ns(/\\w+)*#))\\s*")
            val matcher = pattern.matcher(prefixElements)

            while (matcher.find()) {
                val prefix = matcher.group(2)
                val documentUri = matcher.group(3)

                if (!prefix.isNullOrEmpty() && !documentUri.isNullOrEmpty()) {
                    pageNamespaces.add(OpenGraphNamespace(prefix, documentUri))
                }

                hasOpenGraphSpecification = !prefix.isNullOrEmpty() && prefix.equals("og")
            }
        }

        // if no namespace information exists, add some defaults
        if (!hasOpenGraphSpecification) {
            pageNamespaces.add(OpenGraphNamespace("og", "https://ogp.me/ns#"))
        }

        val metaElements = document.head().getElementsByTag("meta")
        val titleElement = document.head().getElementsByTag("title")
        val openGraphMetaData = OpenGraphMetaData()

        // go through every meta element that we have collected, and try to identify any open graph tags
        // once we have identified them, sort them appropriately into the data structure
        for (metaElement in metaElements) {
            for (namespace in pageNamespaces) {
                var target = ""

                if (metaElement.hasAttr("property")) {
                    target = "property"
                } else if (metaElement.hasAttr("name")) {
                    target = "name"
                }

                if (target.isNotEmpty()
                    && metaElement.attr(target).startsWith(namespace.prefix + ":")
                ) {

                    when (metaElement.attr(target)) {
                        "og:title" -> openGraphMetaData.title = metaElement.attr("content")
                        "og:type" -> openGraphMetaData.type = metaElement.attr("content")
                        "og:image" -> openGraphMetaData.imageUrl = metaElement.attr("content")
                        "og:url" -> openGraphMetaData.url = metaElement.attr("content")
                        "og:description" -> openGraphMetaData.description =
                            metaElement.attr("content")
                    }

                    break
                }
            }
        }

        if (openGraphMetaData.title.isEmpty() && titleElement.count() == 1) {
            openGraphMetaData.title = titleElement[0].childNode(0).toString()
        }

        if (openGraphMetaData.url.isEmpty()) {
            openGraphMetaData.url = link.toString()
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
        if (!connection.contentType.isNullOrEmpty()) {
            val contentType = connection.contentType.lowercase()
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

    fun URL.isImage(): Boolean {
        val possible = arrayOf(".gif", ".jpg", ".jpeg", ".png", ".bmp", ".tiff")

        val index = this.path.lastIndexOf(".")

        if(index == -1) {
            return false
        }

        val extension: String = this.path.substring(index)
        return possible.contains(extension)
    }

    companion object {
        private const val CLOSING_HEAD_STRING = "</head>"
    }
}