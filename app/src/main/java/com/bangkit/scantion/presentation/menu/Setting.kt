package com.bangkit.scantion.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bangkit.scantion.R
import com.bangkit.scantion.presentation.profile.ProfileSpacer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Setting(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>,
    onThemeChange: (Boolean) -> Unit
){
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween) {
        TopAppBar(
            title = {
                Text(
                    text = "Pengaturan",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = "back"
                    )
                }
            },
        )
        ContentSetting(isDarkTheme, onThemeChange)
    }
}

@Composable
fun ContentSetting(isDarkTheme: MutableState<Boolean>, onThemeChange: (Boolean) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        ProfileSpacer()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row{
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_dark_mode), contentDescription = "item icon"
                    )
                    Column(
                        modifier = Modifier.padding(start = 16.dp),
                    ) {
                        Text(
                            text = "Dark Mode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Switch(
                    modifier = Modifier.semantics { contentDescription = "dark mode" },
                    checked = isDarkTheme.value,
                    onCheckedChange = onThemeChange
                )
            }
        }
    }
}