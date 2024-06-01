package com.jlpc.facetimelapsemaker.model

import android.net.Uri
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PhotoRepository(private val database: PhotoDatabase) {
    private val TAG: String = "PhotoRepo"

    suspend fun addPhoto(photoEntity: PhotoEntity) {
        database.photoDao().insertPhoto(photoEntity)
    }

    suspend fun getAllPhotoEntities(): List<PhotoEntity> {
        return database.photoDao().getAllPhotos()
    }

    suspend fun getAllURIs(): List<Uri> {
        return database.photoDao().getAllPhotos().map { Uri.parse(it.uri) }
    }

    suspend fun dbFirst() {
        val firstEntity = database.photoDao().getAllPhotos()[1]
        Log.d(TAG, "First entity is: $firstEntity")
    }

    suspend fun deletePhoto(photoEntity: PhotoEntity) {
        database.photoDao().deletePhoto(photoEntity)
    }

    val allPhotoEntities: Flow<List<PhotoEntity>> =
        // Use Room's Flow mechanism if you're using it for the database
        database.photoDao().getAllPhotosAsFlow()
            .map { photoEntities: List<PhotoEntity>-> photoEntities.sortedBy { it.date } }
}
