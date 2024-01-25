
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jlpc.facetimelapsemaker.FaceTimelapseMakerApp
import com.jlpc.facetimelapsemaker.components.PreviewVideoPlayer
import com.jlpc.facetimelapsemaker.components.VideoPlayer
import com.jlpc.facetimelapsemaker.viewmodel.ResultViewModel
import com.jlpc.facetimelapsemaker.viewmodel.ResultViewModelFactory

@Composable
fun ResultScreen(navController: NavController) {
    val TAG = "ResultScreen"
    val context = LocalContext.current
    val viewModel: ResultViewModel = viewModel(
        factory = ResultViewModelFactory(
            FaceTimelapseMakerApp.repository,
            LocalContext.current.applicationContext,
        ),
    )
    val timelapseFinished = viewModel.timelapseGenerated.observeAsState()

    LaunchedEffect(timelapseFinished.value) {
        Log.d("timelapseStateLiveData", "Live data changed to ${timelapseFinished.value}")
    }

    LaunchedEffect(Unit) {
        viewModel.launchTimelapseCommand()
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (!timelapseFinished.value!!) {
            Log.d(TAG, "timelapse not finished to composable")
            Text("Generating Video...")
        } else {
            Log.d(TAG, "Drawing video player")
            Column() {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    viewModel.uriLiveData.value?.let {
                        VideoPlayer(
                            uri = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.66f)
                                .clip(shape = MaterialTheme.shapes.large),
                        )
                    }
                }
                VideoActionPanel(
                    onSaveButtonClick = { /*TODO*/ },
                    onShareButtonClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "video/*"
                            putExtra(Intent.EXTRA_STREAM, viewModel.uriLiveData.value)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        val shareIntent = Intent.createChooser(intent, "Share your video")
                        context.startActivity(shareIntent)
                    },
                    onNewVideoButtonClick = {},
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewResultScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,

    ) {
        Column() {
            Text("Video")
            Box(modifier = Modifier.height(300.dp).width(300.dp)) {
                PreviewVideoPlayer()
            }
            Text("Sharing options..")
        }
    }
}

// panel consisting of buttons to share, save or create a new video.
@Composable
fun VideoActionPanel(
    onSaveButtonClick: () -> Unit,
    onShareButtonClick: () -> Unit,
    onNewVideoButtonClick: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp)) {
        Row() {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(0.5f).padding(end = 2.dp),
                onClick = onSaveButtonClick,
            ) { Text("Save") }
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth().padding(start = 2.dp),
                onClick = onShareButtonClick,
            ) { Text("Share") }
        }
        Button(
            onClick = onNewVideoButtonClick,
            modifier = Modifier.fillMaxWidth(),
        ) { Text("New Video") }
    }
}
