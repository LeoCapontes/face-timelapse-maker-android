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

suspend fun createTimelapse(fps: Int, path: String, encoder: Encoder, quality: String) {
    val destFileName = "timelapse.$"
    val timelapseCommand = (
        "-y -r $fps -s $quality -i $path/%04d.jpg -c:v ${enumString(encoder)}" +
            " -crf 20 $path/$destFileName"
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

private fun enumString(encoder: Encoder): String {
    if (encoder == Encoder.MP4) {
        return "mpeg4"
    } else if (encoder == Encoder.VP8) {
        return "libvpx"
    }
    return "mpeg4"
}

private fun fileExtensionFromEncoder(encoder: Encoder): String {
    if (encoder == Encoder.MP4) {
        return "mp4"
    }
    return "webm"
}

// to ensure best compatibility with ffmpeg, copy and save the files into cache
// naming each file a 4 digit number in order, i.e. 0001, 0002 etc.
// this should be deleted after timelapse generation to save space
// ASSUMES .jpg PHOTOS
fun saveAsFFMpegCompatible(uriList: List<Uri>, context: Context) {
    Log.d(TAG, "save cache called")
    var fileNo: Int = 1
    uriList.forEach { uri ->
        // Open image source as stream
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            // Format the file name with leading zeros
            val fileName = String.format("%04d.jpg", fileNo)
            val file = File(context.cacheDir, fileName)

            // Create a new file
            file.createNewFile()

            // Copy from source to destination
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
            fileNo++
        }
    }
    // print cache to confirm files have been created
    context.cacheDir.walkTopDown().forEach { Log.d(TAG, it.name) }
}
