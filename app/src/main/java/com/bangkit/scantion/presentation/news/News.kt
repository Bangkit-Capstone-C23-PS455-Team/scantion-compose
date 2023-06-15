package com.bangkit.scantion.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.bangkit.scantion.model.News

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun News(
    navController: NavHostController,
    newsId: Int
) {
    val news = News.getData()[newsId]
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = news.title,
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
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                val list = news.listBody
                items(count = list.size) {
                    if (list[it].first != null){
                        AsyncImage(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            model = ImageRequest.Builder(LocalContext.current).data(list[it].first).crossfade(true)
                                .scale(
                                    Scale.FILL
                                ).build(),
                            contentDescription = "Image News",
                        )
                    }
                    if (list[it].second != null){
                        Text(modifier = Modifier.padding(horizontal = 16.dp), text = list[it].second!!)
                    }
                }
            }
        }
    )
}