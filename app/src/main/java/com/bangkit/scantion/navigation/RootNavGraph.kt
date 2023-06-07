package com.bangkit.scantion.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavGraph(
    navController: NavHostController,
    startDestination: String,
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = startDestination
    ) {
        authNavGraph(navController = navController)
        homeNavGraph(navController = navController, isDarkTheme, onThemeChange)
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val HOME = "home_graph"
}