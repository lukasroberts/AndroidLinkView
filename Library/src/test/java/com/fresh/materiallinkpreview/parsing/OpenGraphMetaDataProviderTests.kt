package com.fresh.materiallinkpreview.parsing

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test
import java.net.MalformedURLException

class OpenGraphMetaDataProviderTests {
    private val metaDataProvider : IOpenGraphMetaDataProvider = OpenGraphMetaDataProvider()

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