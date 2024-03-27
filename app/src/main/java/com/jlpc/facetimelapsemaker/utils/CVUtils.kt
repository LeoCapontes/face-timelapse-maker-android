package com.jlpc.facetimelapsemaker.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream

fun fileToMat(
    context: Context,
    filePath: String,
): Mat? {
    try {
        val file = File(filePath)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            val mat = Mat()
            Utils.bitmapToMat(bitmap, mat)
            return mat
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun copyCascadeFileFromAssets(
    context: Context,
    cascadeFileName: String,
): File {
    val inputStream = context.assets.open("$cascadeFileName")
    val cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE)
    val cascadeFile = File(cascadeDir, cascadeFileName)
    FileOutputStream(cascadeFile).use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    return cascadeFile
}

fun eyeMidPoint(
    context: Context,
    image: Mat,
): Array<Int>? {
    val eyeCascade = CascadeClassifier()
    val mCascadeFile = copyCascadeFileFromAssets(context, "haarcascade_eye.xml")
    eyeCascade.load(mCascadeFile.absolutePath)

    if (eyeCascade.empty()) {
        Log.e("eyeMidPoint", "Failed to load eye cascade")
    } else {
        Log.i("eyeMidPoint", "Loaded eye cascade")
    }

    return null
}
