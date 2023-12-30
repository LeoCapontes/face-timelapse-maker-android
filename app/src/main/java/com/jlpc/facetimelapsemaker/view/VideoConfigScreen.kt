package com.jlpc.facetimelapsemaker.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.components.DropDown
import com.jlpc.facetimelapsemaker.components.FramerateSliderCard
import com.jlpc.facetimelapsemaker.components.VideoParameter
import com.jlpc.facetimelapsemaker.viewmodel.VideoConfigViewModel



@Composable
fun VideoConfigScreen(
    navController: NavController,
    viewModel: VideoConfigViewModel = viewModel(),
) {
    val qualityOptions: Array<String> = arrayOf("480p", "720p", "1080p")
    val formatOptions: Array<String> = arrayOf("GIF", "MP4", "MOV")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DropDown(qualityOptions, viewModel, VideoParameter.QUALITY)
            DropDown(formatOptions, viewModel, VideoParameter.FORMAT)
            FramerateSliderCard(viewModel = viewModel)
            Button(onClick = { navController.navigate(Screen.ResultScreen.route) }) {
                Text("Create Video")
            }
        }
    }
}
