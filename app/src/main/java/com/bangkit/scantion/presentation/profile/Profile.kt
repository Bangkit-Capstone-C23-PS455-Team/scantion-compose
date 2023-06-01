package com.bangkit.scantion.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bangkit.scantion.navigation.Graph
import com.bangkit.scantion.viewmodel.WalktrhoughViewModel

@Composable
fun Profile(
    navController: NavHostController, walktrhoughViewModel: WalktrhoughViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(horizontal = 25.dp)) {
            Text(
                text = "Profile", style = MaterialTheme.typography.headlineLarge
            )
            Button(modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    walktrhoughViewModel.saveOnBoardingState(false)
                    navController.popBackStack()
                    navController.navigate(Graph.AUTHENTICATION)
                }) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}