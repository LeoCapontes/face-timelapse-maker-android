package com.jlpc.facetimelapsemaker.viewmodel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.model.PhotoRepository
import com.jlpc.facetimelapsemaker.model.PreferenceManager
import com.jlpc.facetimelapsemaker.utils.TimelapseParams
import com.jlpc.facetimelapsemaker.utils.createTimelapse
import com.jlpc.facetimelapsemaker.utils.deleteCachedImages
import com.jlpc.facetimelapsemaker.utils.fileExtensionFromEncoder
import com.jlpc.facetimelapsemaker.utils.qualityParam
import com.jlpc.facetimelapsemaker.utils.saveImagesToCache
import com.jlpc.facetimelapsemaker.utils.stringToEncoderEnum
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ResultViewModel(
    private val repository: PhotoRepository = FaceTimelapseMakerApp.repository,
    private val appContext: Context,
) : ViewModel() {
    private val preferenceManager: PreferenceManager = FaceTimelapseMakerApp.preferences
    private val TAG: String = "ResultViewModel"
    val saveVideoLauncher: ActivityResultLauncher<Intent>? = null
    val fpsLiveData: LiveData<Int?> = preferenceManager.fpsFlow.asLiveData()
    val uriLiveData: MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }
    val timelapseGenerated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun launchTimelapseCommand() {
        viewModelScope.launch {
            val uriList = repository.getAllURIs()
            combine(
                preferenceManager.fpsFlow,
                preferenceManager.formatFlow,
                preferenceManager.qualityFlow,
            ) { fps, format, quality ->
                Triple(fps, format, quality)
            }.collect { (fps, format, quality) ->
                if (fps == null || fps == 0 || format == null || quality == null) {
                    Log.d(TAG, "Invalid parameters")
                    return@collect
                }

                try {
                    val params = getTimelapseParams(fps, format, quality, appContext)
                    saveImagesToCache(uriList, appContext)
                    createTimelapse(params)
                    updateLiveData(appContext, params.fileExtension)
                    deleteCachedImages(appContext)
                } catch (e: Exception) {
                    Log.e(TAG, "Error generating timelapse", e)
                }
            }
        }
    }

    private fun getTimelapseParams(
        fps: Int,
        format: String,
        quality: String,
        appContext: Context,
    ): TimelapseParams {
        val formatEnum = stringToEncoderEnum(format)
        val fileExtension = fileExtensionFromEncoder(formatEnum)
        val qualityParam = qualityParam(quality)
        val path = appContext.cacheDir.path
        return TimelapseParams(fps, formatEnum, qualityParam, fileExtension, path)
    }

    private fun updateLiveData(
        appContext: Context,
        fileExtension: String,
    ) {
        uriLiveData.value =
            Uri.parse("${appContext.cacheDir}/timelapse.$fileExtension")
        timelapseGenerated.value = true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun copyFileToDownloads(
        context: Context,
        sourceUri: Uri,
        destFile: File,
    ) {
        val sourceFile = sourceUri.path?.let { File(it) }
        Log.d(TAG, "starting copyFile function with uri: $sourceUri and destination: $destFile")
        val values =
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "timelapse.mp4")
                put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        uri?.let { destinationUri ->
            try {
                resolver.openOutputStream(destinationUri).use { outputStream ->
                    FileInputStream(sourceFile).use { inputStream ->
                        inputStream.copyTo(outputStream!!)
                    }
                }
                Toast.makeText(context, "File copied to Downloads", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

class ResultViewModelFactory(
    private val repository: PhotoRepository,
    private val appContext: Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResultViewModel(repository, appContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
