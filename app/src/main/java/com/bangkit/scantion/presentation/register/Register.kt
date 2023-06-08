package com.bangkit.scantion.presentation.register

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
import androidx.navigation.NavHostController
import com.bangkit.scantion.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.bangkit.scantion.data.repository.UserRepository
import com.bangkit.scantion.model.UserReg
import com.bangkit.scantion.navigation.AuthScreen
import com.bangkit.scantion.ui.component.AuthSpacer
import com.bangkit.scantion.ui.component.AuthTextField
import com.bangkit.scantion.ui.component.ScantionButton

@Composable
fun Register(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp)
    ) {
        TopSection(navController = navController)
        ContentSection(navController = navController)
        BottomSection(navController = navController)
    }
}

@Composable
fun BottomSection(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Sudah punya akun? ")
        Text(text = "Masuk",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
                navController.navigate(AuthScreen.Login.route)
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

@Composable
fun ContentSection(navController: NavHostController) {
    var nameText by rememberSaveable { mutableStateOf("") }
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var confirmPasswordText by rememberSaveable { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth()
    ) {
        Text(
            text = "Halo, Silahkan Daftar Untuk Memulai",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
        AuthSpacer()
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("Nama") },
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_name), contentDescription = "icon tf name")
            }
        )
        AuthSpacer()
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_mail), contentDescription = "icon tf mail")
            }
        )
        AuthSpacer()
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("Password") },
            isPasswordTf = true,
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_password), contentDescription = "icon tf password")
            }
        )
        AuthSpacer()
        AuthTextField(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPasswordText,
            onValueChange = { confirmPasswordText = it },
            label = { Text("Confirm Password") },
            isPasswordTf = true,
            leadingIcon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_password), contentDescription = "icon tf password")
            }
        )
        AuthSpacer()
        ScantionButton(
            enabled = nameText.isNotEmpty() && emailText.isNotEmpty() && passwordText.isNotEmpty() && confirmPasswordText.isNotEmpty() && passwordText == confirmPasswordText,
            onClick = {
                val userReg = UserReg(nameText, emailText, passwordText, 0, "sadf", "asfd")
                val userRepository = UserRepository()
                performRegistration(navController, userRepository, userReg)
            },
            text = stringResource(id = R.string.register_text),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun performRegistration(navController: NavHostController, userRepository: UserRepository, userReg: UserReg) {
    userRepository.registerUser(userReg,
        onSuccess = {
            navController.navigate(AuthScreen.Login.route) {
                popUpTo(AuthScreen.Walkthrough.route)
            }
        },
        onError = {
        }
    )
}