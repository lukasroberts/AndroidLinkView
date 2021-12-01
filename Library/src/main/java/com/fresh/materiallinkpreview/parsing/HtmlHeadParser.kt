package com.fresh.materiallinkpreview.parsing

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class HtmlHeadParser : IHtmlHeadParser {
    override fun getHtmlHeader(link: URL) : String {
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

        return bufferedHeadContents.toString()
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

    companion object {
        private const val CLOSING_HEAD_STRING = "</head>"
    }
}