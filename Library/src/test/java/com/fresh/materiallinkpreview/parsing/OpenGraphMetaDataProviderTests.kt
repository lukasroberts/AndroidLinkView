package com.fresh.materiallinkpreview.parsing

import org.junit.Test

class OpenGraphMetaDataProviderTests {
    private val metaDataProvider : IOpenGraphMetaDataProvider = OpenGraphMetaDataProvider()

    @Test
    fun `parsing works as intended` () {
        metaDataProvider.startFetchingMetadata(java.net.URL("https://www.bbc.co.uk"))
    }
}