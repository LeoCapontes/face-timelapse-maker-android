package com.jlpc.facetimelapsemaker.view

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.R
import com.jlpc.facetimelapsemaker.components.PreviewImportedPhotoGrid

private val TAG: String = "MainScreen"

@Composable
fun HomeScreen(
    onCreateButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
    navController: NavController
){
    Column (){
        Box(modifier = Modifier.fillMaxHeight(0.9f)) {
            PreviewImportedPhotoGrid()
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
    onSettingsButtonClick: () -> Unit
) {
    Row (
        modifier = Modifier.fillMaxWidth()
    ){
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
        ){}
        Spacer(Modifier.weight(0.5f))
    }
}