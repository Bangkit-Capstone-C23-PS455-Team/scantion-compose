package com.bangkit.scantion

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.bangkit.scantion.data.database.SkinExamsDao
import com.bangkit.scantion.data.database.SkinExamsDatabase
import com.bangkit.scantion.navigation.HomeScreen
import com.bangkit.scantion.navigation.RootNavGraph
import com.bangkit.scantion.ui.theme.ScantionTheme
import com.bangkit.scantion.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScantionApp : Application(){
    private var db: SkinExamsDatabase? = null

    companion object {
        private var instance: ScantionApp? = null

        fun getDao(): SkinExamsDao {
            return instance?.getDb()?.SkinExamsDao()
                ?: throw IllegalStateException("ScantionApp instance is not initialized")
        }

        fun getInstance(): ScantionApp {
            return instance ?: throw IllegalStateException("JourneyApp instance is not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getDb(): SkinExamsDatabase {
        return db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                applicationContext,
                SkinExamsDatabase::class.java,
                Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration() // remove in prod
                .build().also { db = it }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScantionAppCompose(screen: String) {
    ScantionTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val backStackEntry = navController.currentBackStackEntryAsState()
            Scaffold(bottomBar = {
                when (backStackEntry.value?.destination?.route) {
                    HomeScreen.Home.route, HomeScreen.Profile.route -> NavBar(navController, backStackEntry)
                }
            }, content = {
                RootNavGraph(navController = navController, startDestination = screen)
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

    Box(contentAlignment = Alignment.Center) {
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
                .height(50.dp),
            onClick = { navController.navigate(navBarItems[1].route) },
        ) {
            Icon(navBarItems[1].icon, contentDescription = "create examination")
        }
    }
}