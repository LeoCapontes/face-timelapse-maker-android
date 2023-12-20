package com.jlpc.facetimelapsemaker.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun FramerateSliderCard(maxFramerate: Float = 24f) {
    var sliderPosition by remember { mutableFloatStateOf(1f) }
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            steps = maxFramerate.toInt() - 1,
            valueRange = 1f..maxFramerate,
        )
        Text(text = "Selected framerate: ${sliderPosition.toInt()} FPS")
    }
}
