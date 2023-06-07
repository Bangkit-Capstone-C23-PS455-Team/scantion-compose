package com.bangkit.scantion.navigation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.bangkit.scantion.presentation.detail.Detail
import com.bangkit.scantion.presentation.examination.Examination
import com.bangkit.scantion.presentation.history.History
import com.bangkit.scantion.presentation.home.Home
import com.bangkit.scantion.presentation.menu.Setting
import com.bangkit.scantion.presentation.profile.Profile

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: (Boolean) -> Unit
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
        composable(route = HomeScreen.Setting.route) {
            Setting(navController, isDarkTheme, onThemeChange)
        }
        composable(route = HomeScreen.History.route) {
            History(navController)
        }
        composable(
            route = "detail/{skinCaseId}",
            arguments = listOf(navArgument("skinCaseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            val skinCaseId = arguments.getString("skinCaseId")
            if (skinCaseId != null){
                Detail(navController, skinCaseId = skinCaseId)
            }
        }

    }
}

sealed class HomeScreen(val route: String) {
    object Home : HomeScreen(route = "home_screen")
    object Profile : HomeScreen(route = "profile_screen")
    object Examination : HomeScreen(route = "examination_screen")
    object Setting: HomeScreen("setting_screen")
    object History : HomeScreen(route = "history_route")
    object Detail: HomeScreen("detail/{storyId}") {
        fun createRoute(skinCaseId: String) = "detail/$skinCaseId"
    }
}