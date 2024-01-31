import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.components.WelcomeCard
import com.jlpc.facetimelapsemaker.model.MetaDataGetter
import com.jlpc.facetimelapsemaker.view.Screen
import com.jlpc.facetimelapsemaker.viewmodel.LandingViewModel

private val TAG: String = "LandingScreen"

@Composable
fun LandingScreen(navController: NavController) {
    val context = LocalContext.current
    val metaDataGetter: MetaDataGetter = MetaDataGetter(context = context)
    val viewModel: LandingViewModel = LandingViewModel(metaDataGetter)

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(),
            onResult = {
                if (it.isEmpty()) {
                    Toast.makeText(
                        context,
                        "No images selected",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Log.d(TAG, "Selected URI: $it")
                    viewModel.updateDB(it)
                    navController.navigate(Screen.HomeScreen.route) {
                        // prevent user from swiping back to landing screen
                        popUpTo(Screen.LandingScreen.route) {
                            inclusive = true
                        }
                    }
                }
            },
        )

    val startPhotoPickerEvent by viewModel.startPhotoPickerEvent.observeAsState()

    LaunchedEffect(startPhotoPickerEvent) {
        startPhotoPickerEvent?.let {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
            )
        }
    }

    Surface {
        WelcomeCard(onButtonClick = { viewModel.onImportButtonClick() })
    }
}
