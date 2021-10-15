package com.fresh.materiallinkpreview.parsing

import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import java.net.URL

interface IOpenGraphMetaDataProvider {
    /**
     * Start the request to asynchronously fetch the open graph metadata
     *
     * @param link - the link for which you would like to receive metadata for
     */
    suspend fun startFetchingMetadataAsync(link: URL) : Result<OpenGraphMetaData>

    /**
     * Start the request to synchronously fetch the open graph metadata
     *
     * @param link - the link for which you would like to receive metadata for
     */
    fun startFetchingMetadata(link: URL) : OpenGraphMetaData?
}