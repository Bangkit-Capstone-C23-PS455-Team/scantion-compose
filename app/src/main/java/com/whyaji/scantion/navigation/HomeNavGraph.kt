package com.whyaji.scantion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.whyaji.scantion.screen.Home

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.HOME,
        startDestination = HomeScreen.Home.route
    ) {
        composable(route = HomeScreen.Home.route) {
            Home(navController)
        }
    }
}

sealed class HomeScreen(val route: String) {
    object Home : HomeScreen(route = "home_screen")
}