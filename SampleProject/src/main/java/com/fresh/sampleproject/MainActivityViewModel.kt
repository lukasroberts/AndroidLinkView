package com.fresh.sampleproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fresh.materiallinkpreview.models.OpenGraphMetaData

class MainActivityViewModel : ViewModel() {

    private val _metaDataList : MutableLiveData<List<OpenGraphMetaData>> = MutableLiveData()
    val metaDataList : LiveData<List<OpenGraphMetaData>> = _metaDataList

    suspend fun downloadMetaData() {

    }
}