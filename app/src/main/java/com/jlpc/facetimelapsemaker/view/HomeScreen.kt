package com.jlpc.facetimelapsemaker.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.R
import com.jlpc.facetimelapsemaker.components.ImportedPhotoGrid
import com.jlpc.facetimelapsemaker.viewmodel.HomeViewModel

private val TAG: String = "MainScreen"

@Composable
fun HomeScreen(
    onCreateButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    navController: NavController,
) {
    val viewModel: HomeViewModel = HomeViewModel()
    val TAG: String = "HomeScreen"
    val photoList by viewModel.currentPhotoList.observeAsState()

    Column {
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            photoList?.let {
                ImportedPhotoGrid(it)
                Log.d(TAG, "photolist attempted")
            } ?: run {
                Text("Loading Photos...")
                Log.d(TAG, "no photolist")
            }
        }
        MainButtonPanel(
            onCreateButtonClick = onCreateButtonClick,
            onSettingsButtonClick = onSettingsButtonClick,
        )
    }
}

@Composable
fun MainButtonPanel(
    onCreateButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Spacer(Modifier.weight(2f))
        Button(
            onClick = onCreateButtonClick,
            shape = FloatingActionButtonDefaults.extendedFabShape,

        ) {
            Text(text = stringResource(R.string.create_timelapse_button))
        }
        Spacer(Modifier.weight(0.5f))
        Button(
            onClick = onSettingsButtonClick,
        ) {}
        Spacer(Modifier.weight(0.5f))
    }
}
