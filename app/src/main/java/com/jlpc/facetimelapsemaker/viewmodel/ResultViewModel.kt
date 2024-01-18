package com.jlpc.facetimelapsemaker.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import com.jlpc.facetimelapsemaker.model.PreferenceManager
import com.jlpc.facetimelapsemaker.utils.Encoder
import com.jlpc.facetimelapsemaker.utils.createTimelapse
import com.jlpc.facetimelapsemaker.utils.saveImagesToCache
import kotlinx.coroutines.launch

// TODO add factory
class ResultViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
    private val appContext: Context,
) : ViewModel() {
    val TAG: String = "ResultViewModel"
    val preferenceManager: PreferenceManager = FaceTimelapseMakerApp.preferences
    val fpsLiveData: LiveData<Int?> = preferenceManager.fpsFlow.asLiveData()
    val uriLiveData: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }
    val timelapseGenerated: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    fun launchTimelapseCommand() {
        viewModelScope.launch {
            val uriList = repository.getAllURIs()

            preferenceManager.fpsFlow.collect { fps ->
                if (fps != null) {
                    saveImagesToCache(uriList, appContext)
                    // TODO change placeholder
                    if (fps != 0) {
                        createTimelapse(fps, appContext.cacheDir.path, Encoder.MP4, "1920x1080")
                        uriLiveData.value = Uri.parse("${appContext.cacheDir}/timelapse.mp4")
                        Log.d(TAG, "timelapse complete")
                        timelapseGenerated.value = true
                    } else {
                        Log.d(TAG, "fps is null")
                    }
                }
            }
            // TODO clear cached images after timelapse generated
        }
    }
}

class ResultViewModelFactory(
    private val repository: PhotoRepository,
    private val appContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(repository, appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}