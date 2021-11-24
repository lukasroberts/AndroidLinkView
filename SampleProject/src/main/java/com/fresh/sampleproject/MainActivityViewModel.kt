package com.fresh.sampleproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresh.materiallinkpreview.models.OpenGraphMetaData
import com.fresh.materiallinkpreview.parsing.IOpenGraphMetaDataProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel
@Inject constructor(private val openGraphMetaDataProvider : IOpenGraphMetaDataProvider) : ViewModel() {
    private val _metaDataList : MutableLiveData<List<OpenGraphMetaData>> = MutableLiveData()
    val metaDataList : LiveData<List<OpenGraphMetaData>> = _metaDataList

    fun downloadMetaData() {
        viewModelScope.launch {
            this.runCatching {
                val mutableOpenGraphList = mutableListOf<OpenGraphMetaData>()

                val facebookResult = openGraphMetaDataProvider.startFetchingMetadataAsync(URL("https://facebook.com"))
                val bbcResult = openGraphMetaDataProvider.startFetchingMetadataAsync(URL("https://bbc.co.uk"))
                val googleResult = openGraphMetaDataProvider.startFetchingMetadataAsync(URL("https://www.google.com"))
                val appleResult = openGraphMetaDataProvider.startFetchingMetadataAsync(URL("https://www.apple.com"))

                if (facebookResult.isSuccess) mutableOpenGraphList.add(facebookResult.getOrNull()!!)
                if (bbcResult.isSuccess) mutableOpenGraphList.add(bbcResult.getOrNull()!!)
                if (googleResult.isSuccess) mutableOpenGraphList.add(googleResult.getOrNull()!!)
                if (appleResult.isSuccess) mutableOpenGraphList.add(appleResult.getOrNull()!!)

                _metaDataList.value = mutableOpenGraphList
            }
        }
    }
}