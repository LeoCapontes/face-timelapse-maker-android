package com.jlpc.facetimelapsemaker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import com.jlpc.facetimelapsemaker.utils.saveAsFFMpegCompatible
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
    private val appContext: Context
) : ViewModel() {
    fun createTimelapse() {
        viewModelScope.launch {
            val uriList = repository.getAllURIs()
            saveAsFFMpegCompatible(uriList, appContext)
        }
    }
}
