package com.whyaji.scantion.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.whyaji.scantion.screen.Walktrhough

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Walkthrough.route
    ) {
        composable(route = AuthScreen.Walkthrough.route) {
            Walktrhough(navController = navController)
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Walkthrough : AuthScreen(route = "walkthrough_screen")
}