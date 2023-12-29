package com.jlpc.facetimelapsemaker.components

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.jlpc.facetimelapsemaker.viewmodel.VideoConfigViewModel

enum class VideoParameter {
    QUALITY, FORMAT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(menuOptions: Array<String> = arrayOf("1", "2", "3"), viewModel: VideoConfigViewModel, param: VideoParameter) {
    val context = LocalContext.current
    val TAG: String = "DropDownComposable"
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(menuOptions[0]) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        TextField(
            value = selectedOption,
            onValueChange = {
            },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            menuOptions.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedOption = item
                        Log.d(TAG, "onClick called")
                        if (param == VideoParameter.FORMAT) {
                            viewModel.saveSelectedFormat(item)
                            Log.d(TAG, "Saved format")
                        } else if (param == VideoParameter.QUALITY) {
                            viewModel.saveSelectedQuality(item)
                            Log.d(TAG, "Saved quality")
                        } else {
                            Log.d(TAG, "no Param matched")
                        }
                        expanded = false
                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                    },
                )
            }
        }
    }
}
