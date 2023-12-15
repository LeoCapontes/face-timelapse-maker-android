package com.jlpc.facetimelapsemaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jlpc.facetimelapsemaker.components.Navigation
import com.jlpc.facetimelapsemaker.view.ui.theme.FaceTimelapseMakerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
            FaceTimelapseMakerTheme {
            }
        }
    }
}
