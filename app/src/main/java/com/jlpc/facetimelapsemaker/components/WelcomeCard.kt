package com.jlpc.facetimelapsemaker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jlpc.facetimelapsemaker.R

@Composable
fun WelcomeCard(onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.landing_header),
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(R.string.landing_import_instructions),
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
            ) {
                Text(text = stringResource(R.string.landing_import_button))
            }
        }
    }
}
