package com.jlpc.facetimelapsemaker.view

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object LandingScreen : Screen("landing_screen")
    data object VideoConfigScreen : Screen("videoconfig_screen")
    data object ResultScreen : Screen("result_screen")
}
