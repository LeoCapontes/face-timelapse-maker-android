package com.jlpc.facetimelapsemaker.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.R
import com.jlpc.facetimelapsemaker.components.ExpandedPhotoContainer
import com.jlpc.facetimelapsemaker.components.ImportedPhotoGrid
import com.jlpc.facetimelapsemaker.model.PhotoEntity
import com.jlpc.facetimelapsemaker.viewmodel.HomeViewModel

private val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    onCreateButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    navController: NavController,
) {
    val viewModel: HomeViewModel = viewModel()
    val TAG = "HomeScreen"
    val photoList by viewModel.currentPhotoList.observeAsState()
    val currentExpandedPhotoEntity by viewModel.currentExpanded.observeAsState()

    val onImageExpand: (PhotoEntity) -> Unit = { entity ->
        viewModel.currentExpanded.value = entity
    }

    Column {
        Box(
            modifier =
            Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .clip(shape = MaterialTheme.shapes.large),
        ) {
            photoList?.let {
                ImportedPhotoGrid(it, onImageExpand)
                Log.d(TAG, "photolist attempted")
            } ?: run {
                Text("Loading Photos...")
                Log.d(TAG, "no photolist")
            }
            MainButtonPanel(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(color = Color.White.copy(alpha = 0.8f)),
                onCreateButtonClick = onCreateButtonClick,
                onSettingsButtonClick = onSettingsButtonClick,
            )
        }
    }
    if (currentExpandedPhotoEntity != null) {
        Box(
            modifier =
                Modifier
                    .background(color = Color.Gray.copy(alpha = 0.5f))
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { viewModel.currentExpanded.value = null })
                    }
        )
        ExpandedPhotoContainer(entity = currentExpandedPhotoEntity!!)
    }
}

@Composable
fun MainButtonPanel(
    modifier: Modifier,
    onCreateButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
) {
    val buttonModifier = Modifier.padding(vertical = 18.dp, horizontal = 0.dp)
    Row(
        modifier = modifier,
    ) {
        Spacer(Modifier.weight(2f))
        Button(
            onClick = onCreateButtonClick,
            shape = FloatingActionButtonDefaults.extendedFabShape,
            modifier = buttonModifier,
        ) {
            Text(text = stringResource(R.string.create_timelapse_button))
        }
        Spacer(Modifier.weight(0.5f))
        Button(
            onClick = onSettingsButtonClick,
            modifier = buttonModifier.alpha(0f),
        ) {}
        Spacer(Modifier.weight(0.5f))
    }
}
