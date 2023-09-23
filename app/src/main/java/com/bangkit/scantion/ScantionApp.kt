package com.bangkit.scantion

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bangkit.scantion.navigation.HomeScreen
import com.bangkit.scantion.navigation.RootNavGraph
import com.bangkit.scantion.ui.theme.ScantionTheme
import com.bangkit.scantion.viewmodel.SettingViewModel
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltAndroidApp
class ScantionApp : Application()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "UnrememberedMutableState"
)
@Composable
fun ScantionAppCompose(
    screen: String,
    darkTheme: Boolean,
    initTheme: Boolean,
    isSystemDarkTheme: Boolean = isSystemInDarkTheme(),
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val isInitTheme = rememberSaveable{mutableStateOf(initTheme)}
    val isDarkTheme = rememberSaveable{mutableStateOf(darkTheme)}


    LaunchedEffect(isInitTheme.value) {
        if (isInitTheme.value) {
            settingViewModel.setDarkMode(isSystemDarkTheme)
            isDarkTheme.value = isSystemDarkTheme
            settingViewModel.setInitTheme(false)
            isInitTheme.value = false
        }
    }

    val onThemeChange: (Boolean) -> Unit = {
        isDarkTheme.value = it
        settingViewModel.setDarkMode(it)
    }

    ScantionTheme (darkTheme = isDarkTheme.value){
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val backStackEntry = navController.currentBackStackEntryAsState()

            Scaffold(bottomBar = {
                when (backStackEntry.value?.destination?.route) {
                    HomeScreen.Home.route, HomeScreen.Profile.route -> {
                        BottomAppBar {
                            NavBar(navController, backStackEntry)
                        }
                    }
                }
            }, content = {
                RootNavGraph(navController = navController, startDestination = screen, isDarkTheme, onThemeChange)
            })
        }
    }
}

data class NavBarItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

@Composable
fun NavBar(
    navController: NavHostController, backStackEntry: State<NavBackStackEntry?>
) {
    val navBarItems = listOf(
        NavBarItem(
            name = "Beranda",
            route = HomeScreen.Home.route,
            icon = Icons.Rounded.Home,
        ),
        NavBarItem(
            name = "Periksa",
            route = HomeScreen.Examination.route,
            icon = ImageVector.vectorResource(id = R.drawable.ic_examination),
        ),
        NavBarItem(
            name = "Profil",
            route = HomeScreen.Profile.route,
            icon = Icons.Rounded.Person,
        ),
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Transparent),
        contentAlignment = Alignment.Center) {
        NavigationBar{
            navBarItems.forEach { item ->
                val selected = item.route == backStackEntry.value?.destination?.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            if (item.name != "Periksa"){
                                navController.popBackStack()
                                navController.navigate(item.route)
                            }
                        }
                    }, label = {
                        if (item.name != "Periksa"){
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }, icon = {
                        if (item.name != "Periksa"){
                            Icon(
                                imageVector = item.icon,
                                contentDescription = "${item.name} Icon",
                            )
                        }
                    }
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(.28f)
                .height(55.dp),
            onClick = { navController.navigate(navBarItems[1].route) },
        ) {
            Icon(navBarItems[1].icon, contentDescription = "create examination")
        }
    }
}