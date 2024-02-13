package com.jlpc.facetimelapsemaker.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSession
import com.arthenica.ffmpegkit.ReturnCode
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

suspend fun createTimelapse(
    fps: Int,
    path: String,
    encoder: Encoder,
    quality: String,
) {
    val fileExtension = fileExtensionFromEncoder(encoder)
    val encoderParam = encoderFFmpegString(encoder)
    val destFileName = "timelapse.$fileExtension"
    val timelapseCommand = (
        "-y -r $fps -i $path/%04d.jpg -vf scale=$quality -c:v $encoderParam -crf 5 " +
            "-pix_fmt yuv420p $path/$destFileName"
    )

    val session: FFmpegSession = FFmpegKit.execute(timelapseCommand)

    // return code handling
    if (ReturnCode.isSuccess(session.returnCode)) {
        Log.d(TAG, "ffmpeg success")
    } else if (ReturnCode.isCancel(session.returnCode)) {
        Log.d(TAG, "ffmpeg cancel")
    } else {
        Log.d(
            TAG,
            String.format(
                "Command failed with state %s and rc %s.%s",
                session.state,
                session.returnCode,
                session.failStackTrace,
            ),
        )
    }
}

enum class Encoder {
    MP4,
    VP8,
}

private fun encoderFFmpegString(encoder: Encoder): String {
    return when (encoder) {
        Encoder.MP4 -> "mpeg4"
        Encoder.VP8 -> "libvpx"
        else -> "mpeg4"
    }
}

fun fileExtensionFromEncoder(encoder: Encoder): String {
    return when (encoder) {
        Encoder.MP4 -> "mp4"
        Encoder.VP8 -> "webm"
    }
}

fun stringToEncoderEnum(format: String): Encoder {
    return when (format) {
        "MP4" -> Encoder.MP4
        "WEBM" -> Encoder.VP8
        else -> Encoder.MP4
    }
}

fun qualityParam(quality: String): String {
    return when (quality) {
        "480p" -> "480x854"
        "720p" -> "720x1280"
        "1080p" -> "1080x1920"
        else -> "720x1280"
    }
}

// to ensure best compatibility with ffmpeg, copy and save the files into cache
// naming each file a 4 digit number in order, i.e. 0001, 0002 etc.
// this should be deleted after timelapse generation to save space
// ASSUMES .jpg PHOTOS
fun saveImagesToCache(
    uriList: List<Uri>,
    context: Context,
) {
    Log.d(TAG, "save cache called")
    var fileNo: Int = 1
    uriList.forEach { uri ->
        // Open image source as stream
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            // Format the file name with leading zeros
            val fileName = String.format("%04d.jpg", fileNo)
            val file = File(context.cacheDir, fileName)
            file.createNewFile()

            // Copy from source to destination
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            fileNo++
        }
    }
    // print cache to confirm files have been created
    context.cacheDir.walkTopDown().forEach { Log.d(TAG, it.name) }
}

fun deleteCachedImages(context: Context) {
    Log.d(TAG, "delete cached images called")
    val cachedImageRegex = Regex("(\\d{4})\\.jpg")
    context.cacheDir.walkTopDown().forEach {
        if (cachedImageRegex.matches(it.name)) {
            Log.d(TAG, "deleting ${it.name}")
            it.delete()
        } else {
            Log.d(TAG, "not deleting ${it.name}")
        }
    }
    Log.d(TAG, "Cache deleting complete")
}
