import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jlpc.facetimelapsemaker.view.HomeScreen
import com.jlpc.facetimelapsemaker.view.Screen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LandingScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                onCreateButtonClick = {}, onSettingsButtonClick = {}, navController = navController)
        }
        composable(route = Screen.LandingScreen.route){
            LandingScreen(navController = navController)
        }
    }
}

