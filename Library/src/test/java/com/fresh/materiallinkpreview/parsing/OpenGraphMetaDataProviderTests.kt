package com.fresh.materiallinkpreview.parsing

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test
import java.net.MalformedURLException

class OpenGraphMetaDataProviderTests {
    private val metaDataProvider : IOpenGraphMetaDataProvider = OpenGraphMetaDataProvider()

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
        val metadata = metaDataProvider.startFetchingMetadata(java.net.URL("https://www.google.com"))

        assertThat(metadata.url).isEqualTo("https://www.google.com")
        assertThat(metadata.title).isEqualTo("Google")
        assertThat(metadata.type).isEqualTo("")
        assertThat(metadata.imageUrl).isEqualTo("")
        assertThat(metadata.description).isNull()
    }

    @Test
    fun `parsing works as intended with the BBC Homepage` () {
        val metadata = metaDataProvider.startFetchingMetadata(java.net.URL("https://www.bbc.co.uk"))

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

        val validHttpRequest = metaDataProvider.startFetchingMetadata(java.net.URL("http://www.bbc.co.uk"))

        assertThat(validHttpRequest.url).isEqualTo("https://www.bbc.co.uk/")
        assertThat(validHttpRequest.title).isEqualTo("BBC - Home")
        assertThat(validHttpRequest.type).isEqualTo("website")
        assertThat(validHttpRequest.imageUrl).isEqualTo("")
        assertThat(validHttpRequest.description).isEqualTo("The best of the BBC, with the latest news and sport headlines, weather, TV & radio highlights and much more from across the whole of BBC Online.")

        val validHttpRequestWithNoWWW = metaDataProvider.startFetchingMetadata(java.net.URL("http://bbc.co.uk"))
        assertThat(validHttpRequestWithNoWWW.url).isEqualTo("https://www.bbc.co.uk/")
        assertThat(validHttpRequestWithNoWWW.title).isEqualTo("BBC - Home")
        assertThat(validHttpRequestWithNoWWW.type).isEqualTo("website")
        assertThat(validHttpRequestWithNoWWW.imageUrl).isEqualTo("")
        assertThat(validHttpRequestWithNoWWW.description).isEqualTo("The best of the BBC, with the latest news and sport headlines, weather, TV & radio highlights and much more from across the whole of BBC Online.")
    }
}