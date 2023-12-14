package com.jlpc.facetimelapsemaker.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jlpc.facetimelapsemaker.mocks.mockUIModelList
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import com.jlpc.facetimelapsemaker.model.PhotoUiModel

@Composable
fun ImportedPhotoGrid(uiModelList: List<PhotoEntity>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(86.dp),
        content = {
            items(uiModelList.size) {
                    index ->
                PhotoContainer(uiModelList[index])
            }
        },
    )
}

@Composable
@Preview
fun PreviewImportedPhotoGrid() {
    val testPhotoList: List<PhotoUiModel> = mockUIModelList

    LazyVerticalGrid(
        columns = GridCells.Adaptive(86.dp),
        content = {
            items(testPhotoList.size) {
                    index ->
                OLDPhotoContainer(testPhotoList[index])
            }
        },
    )
}
