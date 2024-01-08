import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.viewmodel.ResultViewModel

@Composable
fun ResultScreen(navController: NavController) {
    val viewModel = ResultViewModel(appContext = LocalContext.current.applicationContext)
    LaunchedEffect(Unit) {
        viewModel.launchTimelapseCommand()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Generating Video...")
    }
}
