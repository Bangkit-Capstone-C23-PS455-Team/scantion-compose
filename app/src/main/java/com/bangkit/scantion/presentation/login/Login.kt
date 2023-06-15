package com.bangkit.scantion.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import com.bangkit.scantion.navigation.AuthScreen
import com.bangkit.scantion.navigation.HomeScreen
import com.bangkit.scantion.ui.component.AuthSpacer
import com.bangkit.scantion.ui.component.AuthTextField
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navController: NavHostController,
    fromWalkthrough: Boolean = false,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    Scaffold(modifier = Modifier
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = { focusManager.clearFocus() }), topBar = {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = {
                    focusManager.clearFocus()
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back"
                    )
                }
            })
    }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 20.dp),
            contentPadding = innerPadding,
            content = {
                item {
                    ContentSection(navController = navController, loginViewModel, focusManager)
                    BottomSection(navController = navController, fromWalkthrough, focusManager)
                }
            })
    }
}

@Composable
fun BottomSection(
    navController: NavHostController, fromWalkthrough: Boolean, focusManager: FocusManager
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp), horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Belum punya akun? ")
        Text(text = "Daftar sekarang",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                focusManager.clearFocus()
                if (fromWalkthrough) {
                    navController.navigate(AuthScreen.Register.createRoute(false))
                } else {
                    navController.popBackStack()
                }
            })
    }
}

@Composable
fun ContentSection(
    navController: NavHostController, loginViewModel: LoginViewModel, focusManager: FocusManager
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(true) }
    val isLoading = rememberSaveable { mutableStateOf(false) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val buttonEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val performLogin: () -> Unit = {
        loginViewModel.loginUser(emailText, passwordText).observe(lifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }

                    is Resource.Success -> {
                        val token = it.data.accessToken
                        if (!token.isNullOrEmpty()) {
                            focusManager.clearFocus()
                            Log.d("token", token)
                            loginViewModel.saveToken(token.removePrefix("0|"))
                            navController.navigate(Graph.HOME) {
                                popUpTo(HomeScreen.Home.route) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
                        }
                        isLoading.value = false
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, "Error ${it.message}", Toast.LENGTH_LONG).show()
                        isLoading.value = false
                    }
                }
            }
        }
    }

    Text(
        text = "Selamat Datang Kembali",
        style = MaterialTheme.typography.displaySmall,
        fontWeight = FontWeight.Bold
    )
    AuthSpacer()
    AuthTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(emailFocusRequester),
        value = emailText,
        onValueChange = { emailText = it },
        label = { Text("Email") },
        isEmailTf = true,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_mail),
                contentDescription = "icon tf mail"
            )
        },
        nextFocusRequester = passwordFocusRequester
    )
    AuthTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(passwordFocusRequester),
        value = passwordText,
        onValueChange = { passwordText = it },
        label = { Text("Password") },
        isPasswordTf = true,
        leadingIcon = {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_password),
                contentDescription = "icon tf password"
            )
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