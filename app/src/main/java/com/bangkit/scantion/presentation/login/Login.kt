package com.bangkit.scantion.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bangkit.scantion.R
import com.bangkit.scantion.navigation.Graph
import com.bangkit.scantion.viewmodel.LoginViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.bangkit.scantion.model.UserLog
import com.bangkit.scantion.navigation.AuthScreen
import com.bangkit.scantion.ui.component.AuthSpacer
import com.bangkit.scantion.ui.component.AuthTextField
import com.bangkit.scantion.ui.component.ScantionButton
import java.util.UUID

@Composable
fun Login(
    navController: NavHostController,
    fromWalkthrough: Boolean = false,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp)
    ) {
        TopSection(navController = navController)
        ContentSection(navController = navController, loginViewModel)
        BottomSection(navController = navController, fromWalkthrough)
    }
}

@Composable
fun BottomSection(navController: NavHostController, fromWalkthrough: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Belum punya akun? ")
        Text(text = "Daftar sekarang",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                if (fromWalkthrough) {
                    navController.navigate(AuthScreen.Register.createRoute(false))
                } else {
                    navController.popBackStack()
                }
            })
    }
}

@Composable
fun TopSection(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = "back"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentSection(
    navController: NavHostController, loginViewModel: LoginViewModel
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(true) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val buttonEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()

    val performLogin: () -> Unit = {
        val userLog = UserLog(
            token = "123456789",
            id = "id-${UUID.randomUUID()}",
            name = "Alfachri Ghani",
            email = emailText,
            age = -1,
            province = "",
            city = ""
        )
        loginViewModel.saveLoginState(userLog)
        navController.navigate(Graph.HOME) {
            popUpTo(Graph.AUTHENTICATION) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth()
    ) {
        Text(
            text = "Selamat Datang Kembali",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        AuthSpacer()
        AuthTextField(
            modifier = Modifier.fillMaxWidth().focusRequester(emailFocusRequester)
                .onFocusChanged { if (!it.isFocused) emailFocusRequester.freeFocus() },
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_mail), contentDescription = "icon tf mail")
            },
            nextFocusRequester = passwordFocusRequester
        )
        AuthTextField(
            modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester),
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("Password") },
            isPasswordTf = true,
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_password), contentDescription = "icon tf password")
            },
            visibility = passwordVisibility,
            isLast = true,
            buttonEnabled = buttonEnabled,
            performAction = performLogin
        )
        AuthSpacer()
        ScantionButton(
            enabled = buttonEnabled,
            onClick = performLogin,
            text = stringResource(id = R.string.login_text),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}