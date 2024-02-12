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
import com.jlpc.facetimelapsemaker.utils.createTimelapse
import com.jlpc.facetimelapsemaker.utils.deleteCachedImages
import com.jlpc.facetimelapsemaker.utils.fileExtensionFromEncoder
import com.jlpc.facetimelapsemaker.utils.saveImagesToCache
import com.jlpc.facetimelapsemaker.utils.stringToEncoderEnum
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
            preferenceManager.fpsFlow.collect { fps ->
                preferenceManager.formatFlow.collect { format ->
                    val formatEnum = format?.let { stringToEncoderEnum(it) }
                    val fileExtension = formatEnum?.let { fileExtensionFromEncoder(it) }
                    if (fps != null && formatEnum != null) {
                        saveImagesToCache(uriList, appContext)
                        // TODO change placeholder quality
                        if (fps != 0) {
                            createTimelapse(
                                fps,
                                appContext.cacheDir.path,
                                formatEnum,
                                "1920x1080",
                            )
                            uriLiveData.value =
                                Uri.parse(
                                    "${appContext.cacheDir}/timelapse.$fileExtension",
                                )
                            Log.d(TAG, "timelapse complete")
                            timelapseGenerated.value = true
                            deleteCachedImages(appContext)
                        } else {
                            Log.d(TAG, "fps is 0")
                        }
                    }
                }
            }
        }
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
