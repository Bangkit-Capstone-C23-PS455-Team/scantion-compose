package com.bangkit.scantion.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.bangkit.scantion.presentation.detail.Detail
import com.google.accompanist.pager.ExperimentalPagerApi
import com.bangkit.scantion.presentation.login.Login
import com.bangkit.scantion.presentation.register.Register
import com.bangkit.scantion.presentation.walkthrough.Walkthrough

@SuppressLint("ComposableNavGraphInComposeScope")
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
fun NavGraphBuilder.authNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Walkthrough.route
    ) {
        composable(route = AuthScreen.Walkthrough.route) {
            Walkthrough(navController = navController)
        }
        composable(route = "login_screen/{fromWalkthrough}",
            arguments = listOf(navArgument("fromWalkthrough") { type = NavType.BoolType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val fromWalkthrough = arguments.getBoolean("fromWalkthrough")
            Login(navController, fromWalkthrough = fromWalkthrough)
        }

        composable(route = "register_screen/{fromWalkthrough}",
            arguments = listOf(navArgument("fromWalkthrough") { type = NavType.BoolType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val fromWalkthrough = arguments.getBoolean("fromWalkthrough")
            Register(navController, fromWalkthrough = fromWalkthrough)
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Walkthrough : AuthScreen(route = "walkthrough_screen")
    object Login: AuthScreen(route = "login_screen/{fromWalkthrough}") {
        fun createRoute(fromWalkthrough: Boolean) = "login_screen/$fromWalkthrough"
    }
    object Register: AuthScreen(route = "register_screen/{fromWalkthrough}") {
        fun createRoute(fromWalkthrough: Boolean) = "register_screen/$fromWalkthrough"
    }
}