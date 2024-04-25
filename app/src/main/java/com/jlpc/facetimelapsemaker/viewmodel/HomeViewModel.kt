package com.jlpc.facetimelapsemaker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
) : ViewModel() {
    private val TAG: String = "HomeViewModel"

    val currentPhotoList: MutableLiveData<List<PhotoEntity>> by lazy {
        MutableLiveData<List<PhotoEntity>>()
    }

    init {
        Log.d(TAG, "init")
        loadEntities()
    }

    fun loadEntities() {
        viewModelScope.launch {
            currentPhotoList.value = repository.getAllPhotoEntities().sortedBy { it.date }
            Log.d(TAG, "load entities called")
            var temp = currentPhotoList.value
            var repodata = repository.getAllPhotoEntities()
            Log.d(TAG, "current value is $temp")
        }
    }
}
