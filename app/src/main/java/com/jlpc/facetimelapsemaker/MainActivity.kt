package com.jlpc.facetimelapsemaker

import android.media.MediaCodecList
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jlpc.facetimelapsemaker.components.Navigation
import com.jlpc.facetimelapsemaker.view.ui.theme.FaceTimelapseMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
            FaceTimelapseMakerTheme {
            }
        }

        val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
        for (codecInfo in codecList.codecInfos) {
            Log.d("CodecInfo", "Codec: " + codecInfo.name)
        }
    }
}
