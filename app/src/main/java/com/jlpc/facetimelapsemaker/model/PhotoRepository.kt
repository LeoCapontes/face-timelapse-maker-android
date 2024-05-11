package com.jlpc.facetimelapsemaker.model

import android.net.Uri
import android.util.Log

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
}
