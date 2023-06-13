package com.bangkit.scantion.presentation.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bangkit.scantion.DoubleClickBackClose
import com.bangkit.scantion.R
import com.bangkit.scantion.model.ProfileItems
import com.bangkit.scantion.model.UserLog
import com.bangkit.scantion.navigation.Graph
import com.bangkit.scantion.navigation.HomeScreen
import com.bangkit.scantion.ui.component.ConfirmationDialog
import com.bangkit.scantion.viewmodel.HomeViewModel
import com.bangkit.scantion.viewmodel.LoginViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Profile(
    navController: NavHostController, loginViewModel: LoginViewModel = hiltViewModel(), homeViewModel: HomeViewModel = hiltViewModel(),
) {
    var userLog = UserLog()
    try {
        userLog = homeViewModel.userLog.value!!
    } catch (e: Exception){
        navController.popBackStack()
        navController.popBackStack()
        navController.navigate(Graph.AUTHENTICATION)
    }
    DoubleClickBackClose()
    Column(modifier = Modifier.fillMaxSize()) {
        TopSection()
        Spacer(modifier = Modifier.height(20.dp))
        ProfileInfo(userLog)
        Spacer(modifier = Modifier.height(40.dp))
        ContentSection(navController, loginViewModel)
    }
}

@Composable
fun ProfileInfo(userLog: UserLog) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = userLog.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = userLog.email, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ContentSection(navController: NavHostController, loginViewModel: LoginViewModel) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showDialog,
        title = "Apakah anda yakin ingin logout?",
        desc = "Jika anda yakin pilih keluar",
        confirmText = "Keluar",
        dismissText = "Batal",
        redAlert = true,
        onConfirm = {
            loginViewModel.logout()
            navController.popBackStack()
            navController.navigate(Graph.AUTHENTICATION)
        }
    )

    val items = listOf(
        ProfileItems(
            menuName = "Pengaturan",
            desc = "Mengatur tema",
            icon = ImageVector.vectorResource(id = R.drawable.ic_settings),
            action = {
                navController.navigate(HomeScreen.Setting.route)
            }
        ),
        ProfileItems(
            menuName = "Keluar",
            desc = "Keluar dari akun saat ini",
            icon = ImageVector.vectorResource(id = R.drawable.ic_logout),
            action = {
                showDialog.value = true
            }
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        for (item in items){
            RowItemProfileMenu(item = item)
        }
    }
}

@Composable
fun RowItemProfileMenu(item: ProfileItems) {
    ProfileSpacer()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable {
                item.action.invoke()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon, contentDescription = "item icon"
            )
            Column(
                modifier = Modifier.padding(start = 16.dp),
            ) {
                Text(
                    text = item.menuName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.desc,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
fun ProfileSpacer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .alpha(0.3f)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Spacer(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}