package com.jlpc.facetimelapsemaker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import com.jlpc.facetimelapsemaker.utils.Encoder
import com.jlpc.facetimelapsemaker.utils.createTimelapse
import com.jlpc.facetimelapsemaker.utils.saveImagesToCache
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
    private val appContext: Context
) : ViewModel() {
    fun launchTimelapseCommand() {
        viewModelScope.launch {
            val uriList = repository.getAllURIs()
            saveImagesToCache(uriList, appContext)
            //TODO change placeholder
            createTimelapse(10, appContext.cacheDir.path, Encoder.MP4, "1920x1080")
        }
    }
}
