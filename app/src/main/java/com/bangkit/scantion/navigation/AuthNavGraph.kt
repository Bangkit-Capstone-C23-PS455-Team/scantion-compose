package com.bangkit.scantion.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.bangkit.scantion.presentation.login.Login
import com.bangkit.scantion.presentation.register.Register
import com.bangkit.scantion.presentation.walkthrough.Walktrhough

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
        composable(route = AuthScreen.Login.route) {
            Login(navController = navController)
        }
        composable(route = AuthScreen.Register.route) {
            Register(navController = navController)
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Walkthrough : AuthScreen(route = "walkthrough_screen")
    object Login: AuthScreen(route = "login_screen")
    object Register: AuthScreen(route = "register_screen")
}