package com.jlpc.facetimelapsemaker.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.MetaDataGetter
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class LandingViewModel(
    private val metaDataGetter: MetaDataGetter,
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
) : ViewModel() {
    private val TAG: String = "LandingViewModel"
    private val DEFAULTDATE: Long = 1702315783

    // LiveData for event handling
    private val _startPhotoPickerEvent = MutableLiveData<Int>()
    val startPhotoPickerEvent: LiveData<Int> = _startPhotoPickerEvent
    private var eventTrigger = 1

    fun onImportButtonClick() {
        Log.d(TAG, "import button clicked")
        // simple way to create an observable change in state so that button click is always handled
        _startPhotoPickerEvent.value = eventTrigger
        eventTrigger *= -1
    }

    suspend fun getUriDates(uris: List<Uri>?): List<Long> {
        val dateList: MutableList<Long> = mutableListOf()
        withContext(Dispatchers.IO) {
            uris?.forEach { uri ->
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

    fun updateDB(uris: List<Uri>?) {
        viewModelScope.launch(Dispatchers.IO) {
            val uriDates = getUriDates(uris)
            uris?.zip(uriDates)?.forEach { (uri, date) ->
                val photoEntity: PhotoEntity = PhotoEntity(uri = uri.toString(), date = Date(date))
                repository.addPhoto(photoEntity)
            }
            repository.dbFirst()
        }
    }
}
