package com.jlpc.facetimelapsemaker.model

import android.net.Uri

// TODO check if this is even necessary/used anymore
data class PhotoUiModel(
    val date: String,
    val imageUri: Uri?,
) {
    // allow for date sorting using companion object
    companion object : Comparator<PhotoUiModel> {
        override fun compare(
            p1: PhotoUiModel?,
            p2: PhotoUiModel?,
        ): Int {
            if (p1?.imageUri == p2?.imageUri && p1?.date == p2?.date) {
                return 0
            }
            if (p1?.date!! > p2?.date!!) {
                return 1
            }
            return -1
        }
    }
}
