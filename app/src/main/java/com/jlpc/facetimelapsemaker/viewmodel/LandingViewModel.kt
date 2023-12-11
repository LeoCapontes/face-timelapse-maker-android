package com.jlpc.facetimelapsemaker.viewmodel

import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.model.MetaDataGetter
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class LandingViewModel(private val metaDataGetter: MetaDataGetter) : ViewModel() {

    private val TAG: String = "LandingViewModel"
    private val DEFAULTDATE: Long = 1702315783

    // LiveData for event handling
    private val _startPhotoPickerEvent = MutableLiveData<Unit>()
    val startPhotoPickerEvent: LiveData<Unit> = _startPhotoPickerEvent

    fun onImportButtonClick() {
        Log.d(TAG, "import button clicked")
        _startPhotoPickerEvent.value = Unit
    }

    fun getUriDates(uris: List<Uri>?): List<Long>{
        val dateList: MutableList<Long> = mutableListOf()
        viewModelScope.launch(Dispatchers.IO) {
            uris?.forEach {uri ->
                val date = metaDataGetter.getPhotoDate(uri)
                if (date != null) {
                    dateList.add(date)
                } else {
                    dateList.add(DEFAULTDATE)
                }
                Log.d(TAG, "Date is $date")
            }
        }
        return dateList
    }

    fun updateDB(uris: List<Uri>?){
        val uriDates = getUriDates(uris)
        uris?.zip(uriDates)?.forEach { (uri, date) ->
            val photoEntity: PhotoEntity = PhotoEntity(uri = uri.toString(), date = Date(date))

        }
    }
}