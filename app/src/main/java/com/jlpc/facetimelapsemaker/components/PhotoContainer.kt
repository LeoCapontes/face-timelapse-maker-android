package com.jlpc.facetimelapsemaker.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jlpc.facetimelapsemaker.R
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import com.jlpc.facetimelapsemaker.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PhotoContainer(
    uiModel: PhotoEntity,
    viewModel: HomeViewModel
) {
    val TAG = "PhotoContainer"
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier =
        Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(shape = MaterialTheme.shapes.small)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        coroutineScope.launch {
                            viewModel.deletePhoto(uiModel)}
                        Log.d(TAG, "Long pressed")
                    },
                )
            },
    ) {
        AsyncImage(
            model = uiModel.uri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        // container for date overlay
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(color = Color.Black.copy(alpha = 0.3f))
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val height = (constraints.maxHeight * 0.25).toInt()
                        layout(constraints.maxWidth, height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
        ) {
            Text(
                text = formatDate(uiModel.date),
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                // temp solution to center text vertically, as box contentAlignment not working.
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}

fun formatDate(date: Date): String {
    val dateFormat = SimpleDateFormat("dd/MM/yy")
    return dateFormat.format(date)
}

@Preview
@Composable
fun PreviewPhotoContainer() {
    Box(
        modifier =
            Modifier
                .size(64.dp, 64.dp)
                .clip(shape = RoundedCornerShape(4.dp)),
    ) {
        Image(
            painter = painterResource(R.drawable.frog),
            contentDescription = "frog",
        )
        // container for date overlay
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(color = Color.Black.copy(alpha = 0.3f))
                    .align(Alignment.Center)
                    .layout {
                            measurable, constraints ->
                        val placeable = measurable.measure(constraints)

                        val height = (constraints.maxHeight * 0.25).toInt()

                        layout(constraints.maxWidth, height) {
                            placeable.placeRelative(0, 0)
                        }
                    },
        ) {
            Text(
                text = "frog",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
