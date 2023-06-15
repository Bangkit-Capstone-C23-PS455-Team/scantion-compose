package com.bangkit.scantion.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.bangkit.scantion.model.News
import com.bangkit.scantion.navigation.HomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarouselNews(
    navController: NavController,
    newsList: List<News>
) {
    val carouselState: PagerState = rememberPagerState()

    val autoSlideEnabled = remember {mutableStateOf(true)}
    val autoSlideInterval = 4000L

    LaunchedEffect(autoSlideEnabled.value) {
        if (autoSlideEnabled.value) {
            while (true) {
                delay(autoSlideInterval)
                carouselState.animateScrollToPage((carouselState.currentPage + 1) % newsList.size)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(10.dp)) {
        HorizontalPager(
            state = carouselState,
            count = newsList.size,
            contentPadding = PaddingValues(horizontal = 65.dp),
            modifier = Modifier.height(180.dp)
        ) { page ->
            Card(shape = MaterialTheme.shapes.large,
            modifier = Modifier.clickable {
                navController.navigate(HomeScreen.News.createRoute(page))
            }) {
                ImageContainer(newsList, page)
            }
        }
        DotsIndicator(totalDots = newsList.size, selectedIndex = carouselState.currentPage)
    }
}

@Composable
private fun ImageContainer(newsList: List<News>, page: Int) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(newsList[page].thumb).crossfade(true).scale(Scale.FILL).build(),
        contentDescription = "Image News",
    )
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), horizontalArrangement = Arrangement.Center
    ) {

        items(totalDots) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}