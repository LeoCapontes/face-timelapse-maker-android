package com.jlpc.facetimelapsemaker.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.components.FramerateSliderCard

@Composable
fun VideoConfigScreen(
    navController: NavController,
) {
    Column {
        FramerateSliderCard()
    }
}
