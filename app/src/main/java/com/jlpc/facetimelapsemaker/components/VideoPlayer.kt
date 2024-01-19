package com.jlpc.facetimelapsemaker.components

import android.net.Uri
import android.view.LayoutInflater
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.jlpc.facetimelapsemaker.R

@Composable
fun VideoPlayer(modifier: Modifier = Modifier, uri: Uri) {
    val context = LocalContext.current
    val mediaController = remember {MediaController(context)}
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.video_view_layout, null, false) as VideoView
            view.setVideoURI(uri)
            view.setMediaController(mediaController)
            view
        },
        update = {},
    )
}

@Preview
@Composable
fun PreviewVideoPlayer() {
    val uri = Uri.parse("${LocalContext.current.cacheDir}/test.mp4")
    val modifier = Modifier.fillMaxSize()
    //VideoPlayer(modifier = modifier, uri = uri)
}
