package com.jlpc.facetimelapsemaker.utils

import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.FFmpegSession
import com.arthenica.ffmpegkit.ReturnCode

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
