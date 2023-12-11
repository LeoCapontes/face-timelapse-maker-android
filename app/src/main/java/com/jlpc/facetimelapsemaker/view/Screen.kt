package com.jlpc.facetimelapsemaker.view

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("home_screen")
    data object LandingScreen : Screen("landing_screen")
}
