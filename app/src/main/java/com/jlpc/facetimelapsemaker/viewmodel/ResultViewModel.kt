package com.jlpc.facetimelapsemaker.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import com.jlpc.facetimelapsemaker.model.PreferenceManager
import com.jlpc.facetimelapsemaker.utils.Encoder
import com.jlpc.facetimelapsemaker.utils.createTimelapse
import com.jlpc.facetimelapsemaker.utils.saveImagesToCache
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
    private val appContext: Context,
) : ViewModel() {
    val TAG: String = "ResultViewModel"
    val preferenceManager: PreferenceManager = FaceTimelapseMakerApp.preferences
    val fpsLiveData: LiveData<Int?> = preferenceManager.fpsFlow.asLiveData()

    fun launchTimelapseCommand() {
        viewModelScope.launch {
            val uriList = repository.getAllURIs()

            preferenceManager.fpsFlow.collect { fps ->
                if (fps != null) {
                    saveImagesToCache(uriList, appContext)
                    // TODO change placeholder
                    if (fps != 0) {
                        createTimelapse(fps, appContext.cacheDir.path, Encoder.MP4, "1920x1080")
                    } else {
                        Log.d(TAG, "fps is null")
                    }
                }
            }
            // TODO clear cache after timelapse generated
        }
    }
}
