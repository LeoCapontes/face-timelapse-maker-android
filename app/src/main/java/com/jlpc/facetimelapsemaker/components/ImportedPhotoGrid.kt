package com.jlpc.facetimelapsemaker.components

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jlpc.facetimelapsemaker.model.PhotoEntity

@Composable
fun ImportedPhotoGrid(
    photoEntityList: List<PhotoEntity>,
    onExpandImage: (PhotoEntity) -> Unit,
) {
    val onImageExpand: (PhotoEntity) -> Unit = { entity ->
        onExpandImage(entity)
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(124.dp),
        content = {
            items(photoEntityList) {
                    photo ->
                PhotoContainer(photo, onImageExpand)
            }
        },
    )
}

@Preview
@Preview(name = "NEXUS_7", device = Devices.NEXUS_7)
@Preview(name = "PIXEL_4", device = Devices.PIXEL_4)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview()
@Composable
fun PreviewPhotoGrid() {
    // ImportedPhotoGrid(photoEntityList = mockPhotoEntityList)
}
