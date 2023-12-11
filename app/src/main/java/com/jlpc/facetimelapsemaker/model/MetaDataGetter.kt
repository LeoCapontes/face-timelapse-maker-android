package com.jlpc.facetimelapsemaker.model

import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class MetaDataGetter(private val context: Context) {
    fun getPhotoDate(uri: Uri): Long? {
        return try {
            context.contentResolver.query(
                uri,
                arrayOf(MediaStore.Images.Media.DATE_TAKEN),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val dateIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                    cursor.getLong(dateIndex)
                } else null
            }
            }catch (e: Exception) {
                null
        }
    }
}