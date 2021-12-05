package com.fresh.materiallinkpreview.parsing

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import java.net.MalformedURLException

class OpenGraphMetaDataProviderTests {
    private lateinit var mockHtmlHeadParser : IHtmlHeadParser
    private lateinit var metaDataProvider : IOpenGraphMetaDataProvider
    private lateinit var bbcHeader : String
    private lateinit var googleHeader : String

    @Before
    fun setup() {
        bbcHeader = ClassLoader.getSystemResource("bbc-header.html").readText()
        googleHeader = ClassLoader.getSystemResource("google-header.html").readText()

        mockHtmlHeadParser = mockk()
        metaDataProvider = OpenGraphMetaDataProvider(mockHtmlHeadParser)
    }

    @Test
    fun `parsing works as intended with a music link` () {

    }

    @Test
    fun `parsing works as intended with a video link` () {

    }

    @Test
    fun `parsing works as intended with an image link` () {
        val metadata = metaDataProvider.startFetchingMetadata(java.net.URL("https://storage.googleapis.com/gd-wagtail-prod-assets/original_images/evolving_google_identity_2x1.jpg"))

        assertThat(metadata.url).isEqualTo("https://storage.googleapis.com/gd-wagtail-prod-assets/original_images/evolving_google_identity_2x1.jpg")
        assertThat(metadata.title).isEqualTo("")
        assertThat(metadata.type).isEqualTo("website")
        assertThat(metadata.imageUrl).isEqualTo("https://storage.googleapis.com/gd-wagtail-prod-assets/original_images/evolving_google_identity_2x1.jpg")
        assertThat(metadata.description).isNull()
    }

    @Test
    fun `parsing works as intended with the Google Homepage` () {
        val url = java.net.URL("https://www.google.com")
        every { mockHtmlHeadParser.getHtmlHeader(url) }.returns(googleHeader)
        val metadata = metaDataProvider.startFetchingMetadata(url)

        assertThat(metadata.url).isEqualTo("https://www.google.com/logos/doodles/2021/seasonal-holidays-2021-6753651837109324-2xa.gif")
        assertThat(metadata.title).isEqualTo("Google")
        assertThat(metadata.type).isEqualTo("video.other")
        assertThat(metadata.imageUrl).isEqualTo("https://www.google.com/logos/doodles/2021/seasonal-holidays-2021-6753651837109324-2xa.gif")
        assertThat(metadata.description).isEqualTo("Festive Season 2021 #GoogleDoodle")
    }

    @Test
    fun `parsing works as intended with the BBC Homepage` () {
        val url = java.net.URL("https://www.bbc.co.uk")
        every { mockHtmlHeadParser.getHtmlHeader(url) }.returns(bbcHeader)
        val metadata = metaDataProvider.startFetchingMetadata(url)

        assertThat(metadata.url).isEqualTo("https://www.bbc.co.uk/")
        assertThat(metadata.title).isEqualTo("BBC - Home")
        assertThat(metadata.type).isEqualTo("website")
        assertThat(metadata.imageUrl).isEqualTo("")
        assertThat(metadata.description).isEqualTo("The best of the BBC, with the latest news and sport headlines, weather, TV & radio highlights and much more from across the whole of BBC Online.")
    }

    @Test
    fun `parsing works as intended with variants of website names` () {
        try {
            metaDataProvider.startFetchingMetadata(java.net.URL("bbc.co.uk"))
            fail("The above should throw a malformed URL exception")
        } catch (e : MalformedURLException) {
        }

        try {
            metaDataProvider.startFetchingMetadata(java.net.URL("www.bbc.co.uk"))
            fail("The above should throw a malformed URL exception")
        } catch (e : MalformedURLException) {
        }

        val url = java.net.URL("http://www.bbc.co.uk")
        every { mockHtmlHeadParser.getHtmlHeader(url) }.returns(bbcHeader)
        val validHttpRequest = metaDataProvider.startFetchingMetadata(url)

        assertThat(validHttpRequest.url).isEqualTo("https://www.bbc.co.uk/")
        assertThat(validHttpRequest.title).isEqualTo("BBC - Home")
        assertThat(validHttpRequest.type).isEqualTo("website")
        assertThat(validHttpRequest.imageUrl).isEqualTo("")
        assertThat(validHttpRequest.description).isEqualTo("The best of the BBC, with the latest news and sport headlines, weather, TV & radio highlights and much more from across the whole of BBC Online.")

        val noWWW = java.net.URL("http://bbc.co.uk")
        every { mockHtmlHeadParser.getHtmlHeader(noWWW) }.returns(bbcHeader)
        val validHttpRequestWithNoWWW = metaDataProvider.startFetchingMetadata(noWWW)

        assertThat(validHttpRequestWithNoWWW.url).isEqualTo("https://www.bbc.co.uk/")
        assertThat(validHttpRequestWithNoWWW.title).isEqualTo("BBC - Home")
        assertThat(validHttpRequestWithNoWWW.type).isEqualTo("website")
        assertThat(validHttpRequestWithNoWWW.imageUrl).isEqualTo("")
        assertThat(validHttpRequestWithNoWWW.description).isEqualTo("The best of the BBC, with the latest news and sport headlines, weather, TV & radio highlights and much more from across the whole of BBC Online.")
    }
}