package com.jlpc.facetimelapsemaker.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jlpc.facetimelapsemaker.model.PhotoEntity

@Composable
fun ExpandedPhotoContainer(
    entity: PhotoEntity,
) {
    var scale: Float by remember { mutableFloatStateOf(0.2f) }
    Box(
        contentAlignment = Alignment.Center,

        modifier = Modifier
            .padding(64.dp)
            .fillMaxSize(scale)
            .aspectRatio(1f)
            .clip(shape = MaterialTheme.shapes.large)
            .animateContentSize()
    ) {
        AsyncImage(
            model = entity.uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    LaunchedEffect(true) {
        scale = 1f
    }
}