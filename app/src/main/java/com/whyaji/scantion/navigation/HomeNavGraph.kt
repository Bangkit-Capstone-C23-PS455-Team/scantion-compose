package com.whyaji.scantion.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.whyaji.scantion.screen.Examination
import com.whyaji.scantion.screen.Home
import com.whyaji.scantion.screen.Profile

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
        composable(route = HomeScreen.Profile.route) {
            Profile(navController)
        }
        composable(route = HomeScreen.Examination.route) {
            Examination(navController)
        }
    }
}

sealed class HomeScreen(val route: String) {
    object Home : HomeScreen(route = "home_screen")
    object Profile : HomeScreen(route = "profile_screen")
    object Examination : HomeScreen(route = "examination_screen")
}