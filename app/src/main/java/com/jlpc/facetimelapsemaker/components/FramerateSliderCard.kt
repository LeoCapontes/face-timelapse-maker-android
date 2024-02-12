package com.jlpc.facetimelapsemaker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jlpc.facetimelapsemaker.viewmodel.VideoConfigViewModel
import kotlinx.coroutines.flow.map

@Composable
fun FramerateSliderCard(
    maxFramerate: Float = 24f,
    viewModel: VideoConfigViewModel,
) {
    var sliderPosition by remember { mutableFloatStateOf(1f) }
    val fpsPreference: Float =
        viewModel.preferenceManager.fpsFlow
            .map { it?.toFloat() ?: 1f }
            .collectAsState(initial = 1f).value
    sliderPosition = fpsPreference
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                viewModel.saveSelectedFPS(it.toInt())
            },
            steps = maxFramerate.toInt() - 1,
            valueRange = 1f..maxFramerate,
        )
        Text(text = "Selected framerate: ${sliderPosition.toInt()} FPS")
    }
}
