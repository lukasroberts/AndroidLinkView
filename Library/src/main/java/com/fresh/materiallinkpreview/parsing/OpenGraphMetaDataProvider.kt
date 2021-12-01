package com.fresh.materiallinkpreview.parsing

import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.models.OpenGraphNamespace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.URL
import java.util.regex.Pattern

class OpenGraphMetaDataProvider : IOpenGraphMetaDataProvider {
    constructor() {
        _htmlHeadParser = HtmlHeadParser()
    }

    constructor(htmlHeadParser : IHtmlHeadParser) {
        _htmlHeadParser = htmlHeadParser
    }

    private val _pageNamespaces = mutableListOf<OpenGraphNamespace>()
    private val _htmlHeadParser : IHtmlHeadParser

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

        val completeHeadContents = _htmlHeadParser.getHtmlHeader(link)
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
                    _pageNamespaces.add(OpenGraphNamespace(prefix, documentUri))
                }

                hasOpenGraphSpecification = !prefix.isNullOrEmpty() && prefix.equals("og")
            }
        }

        // if no namespace information exists, add some defaults
        if (!hasOpenGraphSpecification) {
            _pageNamespaces.add(OpenGraphNamespace("og", "https://ogp.me/ns#"))
        }

        val metaElements = document.head().getElementsByTag("meta")
        val titleElement = document.head().getElementsByTag("title")
        val openGraphMetaData = OpenGraphMetaData()

        // go through every meta element that we have collected, and try to identify any open graph tags
        // once we have identified them, sort them appropriately into the data structure
        for (metaElement in metaElements) {
            for (namespace in _pageNamespaces) {
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

    fun URL.isImage(): Boolean {
        val possible = arrayOf(".gif", ".jpg", ".jpeg", ".png", ".bmp", ".tiff")

        val index = this.path.lastIndexOf(".")

        if(index == -1) {
            return false
        }

        val extension: String = this.path.substring(index)
        return possible.contains(extension)
    }
}