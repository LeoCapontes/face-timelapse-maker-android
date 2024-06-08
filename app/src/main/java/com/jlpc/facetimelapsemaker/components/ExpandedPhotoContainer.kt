package com.jlpc.facetimelapsemaker.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jlpc.facetimelapsemaker.model.PhotoEntity

@Composable
fun ExpandedPhotoContainer(
    entity: PhotoEntity,

) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
        Modifier
            .padding(64.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape = MaterialTheme.shapes.large)
            .shadow(4.dp)
            .pointerInput(Unit) {
                detectTapGestures()
            },
    ) {
        AsyncImage(
            model = entity.uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}