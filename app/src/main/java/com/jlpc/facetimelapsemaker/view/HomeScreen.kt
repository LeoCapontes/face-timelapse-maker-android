package com.jlpc.facetimelapsemaker.view

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jlpc.facetimelapsemaker.R
import com.jlpc.facetimelapsemaker.components.ImportedPhotoGrid
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
    val currentExpandedUri by viewModel.currentExpanded.observeAsState()

    val onImageExpand: (Uri) -> Unit = { uri ->
        viewModel.currentExpanded.value = uri
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
    if (currentExpandedUri != null) {
        Box(
            modifier =
                Modifier.background(color = Color.Gray.copy(alpha = 0.5f)).fillMaxSize(),
        )
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
                model = currentExpandedUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
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
