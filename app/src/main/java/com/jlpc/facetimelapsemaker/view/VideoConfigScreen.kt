package com.jlpc.facetimelapsemaker.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.components.DropDown
import com.jlpc.facetimelapsemaker.components.FramerateSliderCard

@Composable
fun VideoConfigScreen(
    navController: NavController,
) {
    val qualityOptions: Array<String> = arrayOf("480p", "720p", "1080p")
    val formatOptions: Array<String> = arrayOf("GIF", "MP4", "MOV")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            DropDown(qualityOptions)
            DropDown(formatOptions)
            FramerateSliderCard()
        }
    }
}
