package com.jlpc.facetimelapsemaker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PreferenceManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch

class VideoConfigViewModel() : ViewModel() {
    val TAG: String = "VideoConfigViewModel"
    val preferenceManager: PreferenceManager = FaceTimelapseMakerApp.preferences
    val qualityPreference: LiveData<String?> = preferenceManager.qualityFlow.asLiveData()
    val fpsPreference: LiveData<Int?> = preferenceManager.fpsFlow.asLiveData()

    fun saveSelectedFormat(newFormat: String) {
        viewModelScope.launch {
            preferenceManager.saveFormat(newFormat)
            Log.d(TAG, "saved format $newFormat")
        }
    }

    fun saveSelectedFPS(newFPS: Int) {
        viewModelScope.launch {
            Log.d(TAG, "Attempting to save $newFPS")
            preferenceManager.saveFPS(newFPS)
            preferenceManager.fpsFlow.collect { value ->
                Log.d(TAG, "fps flow saved value: $value")
            }
        }
    }

    fun saveSelectedQuality(newQuality: String) {
        viewModelScope.launch {
            preferenceManager.saveQuality(newQuality)
            Log.d(TAG, "saved quality $qualityPreference")
        }
    }
}
