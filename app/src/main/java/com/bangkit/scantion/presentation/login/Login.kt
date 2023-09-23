package com.bangkit.scantion.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bangkit.scantion.R
import com.bangkit.scantion.navigation.Graph
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import com.bangkit.scantion.navigation.AuthScreen
import com.bangkit.scantion.ui.component.AuthSpacer
import com.bangkit.scantion.ui.component.AuthTextField
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.Resource
import com.bangkit.scantion.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    navController: NavHostController,
    fromWalkthrough: Boolean = false,
    loginViewModel: AuthViewModel = hiltViewModel()
) {
    val isLoading = rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                contentPadding = innerPadding,
                content = {
                    item {
                        ContentSection(
                            navController = navController,
                            loginViewModel,
                            focusManager,
                            isLoading
                        )
                        BottomSection(navController = navController, fromWalkthrough, focusManager)
                    }
                }
            )
        }
        if (isLoading.value){
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .clickable {  }
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = .8f)))
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun BottomSection(
    navController: NavHostController,
    fromWalkthrough: Boolean,
    focusManager: FocusManager,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp), horizontalArrangement = Arrangement.Center
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
    navController: NavHostController,
    viewModel: AuthViewModel,
    focusManager: FocusManager,
    isLoading: MutableState<Boolean>
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(true) }

    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val buttonEnabled = emailText.isNotEmpty() && passwordText.isNotEmpty()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val performLogin: () -> Unit = {
        focusManager.clearFocus()
        viewModel.login(emailText, passwordText).observe(lifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        isLoading.value = true
                    }

                    is Resource.Success -> {
                        navController.navigate(Graph.HOME){
                            popUpTo(Graph.AUTHENTICATION)
                        }
                        isLoading.value = false
                        Toast.makeText(context, "Login Berhasil", Toast.LENGTH_LONG).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
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
        nextFocusRequester = passwordFocusRequester,
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