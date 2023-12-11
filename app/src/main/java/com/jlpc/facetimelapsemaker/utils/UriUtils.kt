package com.jlpc.facetimelapsemaker.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.documentfile.provider.DocumentFile

val TAG: String = "UriUtils"

fun uriListFromStrings(uriStrings: ArrayList<String>): MutableList<Uri> {
    val uriList: MutableList<Uri> = mutableListOf<Uri>()

    uriStrings.forEach{
        if(it.contains(".jpeg") or
            it.contains(".jpg") or
            it.contains(".PNG") or
            it.contains(".png")) {
            uriList.add(Uri.parse(it))
        }
    }
    return uriList
}

fun uriListToStringArray(uriList: MutableList<Uri>) : ArrayList<String> {
    val uriStringArray: ArrayList<String> = ArrayList<String>()
    uriList.forEach {
        uriStringArray.add(it.toString())
    }
    return uriStringArray
}

fun photoUrisFromDir(context: Context, directoryUri: Uri): MutableList<Uri> {
    val uriList: MutableList<Uri> = mutableListOf<Uri>()
    val filesInDirectory = DocumentFile.fromTreeUri(context, directoryUri)
    val files = filesInDirectory?.listFiles()

    files?.forEach {
        uriList.add(it.uri)
    }

    Log.d(TAG, "uris from dir called")
    return uriList
}