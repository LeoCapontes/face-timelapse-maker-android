package com.jlpc.facetimelapsemaker.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert
    suspend fun insertPhoto(photoEntity: PhotoEntity)

    @Query("SELECT * FROM PhotoEntity")
    suspend fun getAllPhotos(): List<PhotoEntity>

    @Delete
    suspend fun deletePhoto(photoEntity: PhotoEntity): Int
}
