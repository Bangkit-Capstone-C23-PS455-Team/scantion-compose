package com.bangkit.scantion.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.bangkit.scantion.model.News

@Composable
fun News(
    navController: NavHostController,
    newsId: Int
) {
    val news = News.getData()[newsId]

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(navController = navController, newsTitle = news.title)
        ContentSection(news = news)
    }
}

@Composable
fun ContentSection(news: News) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AsyncImage(
            modifier = Modifier.padding(horizontal = 16.dp),
            model = ImageRequest.Builder(LocalContext.current).data(news.image).crossfade(true)
                .scale(
                    Scale.FILL
                ).build(),
            contentDescription = "Image News",
        )
        Text(modifier = Modifier.padding(horizontal = 16.dp), text = news.body)
    }
}

@Composable
fun TopSection(navController: NavController, newsTitle: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = "close"
                )
            }
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = newsTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}