package com.jlpc.facetimelapsemaker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
) : ViewModel() {
    private val TAG: String = "HomeViewModel"

    val currentPhotoList: MutableLiveData<List<PhotoEntity>> by lazy {
        MutableLiveData<List<PhotoEntity>>(emptyList()) // Initial state
    }

    val currentExpanded: MutableLiveData<PhotoEntity?> by lazy {
        MutableLiveData<PhotoEntity?>(null)
    }

    init {
        viewModelScope.launch {
            repository.allPhotoEntities.collectLatest { updatedPhotoEntities ->
                currentPhotoList.value = updatedPhotoEntities
                // Log the update to debug if you would like to
                Log.d(TAG, "Photo list updated in the viewModel: $updatedPhotoEntities")
            }
        }
    }

    suspend fun deletePhoto(photoEntity: PhotoEntity) {
        viewModelScope.launch {
            Log.d(TAG, "Called delete photo")
            repository.deletePhoto(photoEntity)
            Log.d(TAG, "Remaining entities: ${repository.getAllPhotoEntities()}")
        }
    }
}
