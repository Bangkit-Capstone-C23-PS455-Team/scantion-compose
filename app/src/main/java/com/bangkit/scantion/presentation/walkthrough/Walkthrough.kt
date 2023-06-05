package com.bangkit.scantion.presentation.walkthrough

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import com.bangkit.scantion.navigation.AuthScreen
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.model.WalkthroughItems
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun Walkthrough(
    navController: NavHostController
) {
    val items = WalkthroughItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(pageState.currentPage < items.size - 1, onSkipClick = {
            if (pageState.currentPage + 1 < items.size) scope.launch {
                pageState.animateScrollToPage(items.size - 1)
            }
        })

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            WalkthroughItem(items = items[page], page, navController)
        }

        BottomSection(size = items.size, index = pageState.currentPage, onNextClick = {
            if (pageState.currentPage < items.size - 1) scope.launch {
                pageState.animateScrollToPage(pageState.currentPage + 1)
            }
        }, onPrevClick = {
            if (pageState.currentPage + 1 > 1) scope.launch {
                pageState.animateScrollToPage(pageState.currentPage - 1)
            }
        })
    }
}

@Composable
fun TopSection(visible: Boolean, onSkipClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 15.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        AnimatedVisibility(
            visible = visible, enter = fadeIn(), exit = fadeOut()
        ) {
            TextButton(
                onClick = onSkipClick, contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "Skip")
            }
        }
    }
}

@Composable
fun BottomSection(
    size: Int, index: Int, onNextClick: () -> Unit = {}, onPrevClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Previous Button
            IconButton(
                onClick = {
                    if (index > 0) {
                        onPrevClick.invoke()
                    }
                }, enabled = index > 0
            ) {
                AnimatedVisibility(
                    index > 0, enter = fadeIn(), exit = fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = null
                    )
                }
            }

            // Indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(size) {
                    Indicator(isSelected = it == index)
                }
            }

            // Next Button
            IconButton(
                onClick = {
                    if (index < size - 1) {
                        onNextClick.invoke()
                    }
                }, enabled = index < size - 1
            ) {
                AnimatedVisibility(
                    visible = index < size - 1, enter = fadeIn(), exit = fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight, contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            )
    ) {

    }
}

@Composable
fun WalkthroughItem(
    items: WalkthroughItems, page: Int, navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = items.image),
            contentDescription = "Image1",
        )

        if (page == 3) {
            Column(
                modifier = Modifier.height(150.dp), verticalArrangement = Arrangement.Center
            ) {
                ScantionButton(
                    onClick = {
                        navController.navigate(AuthScreen.Login.route)
                    },
                    text = stringResource(id = items.textFirst),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                ScantionButton(
                    onClick = { navController.navigate(AuthScreen.Register.route) },
                    text = stringResource(id = items.textSecond),
                    modifier = Modifier.fillMaxWidth(),
                    outlineButton = true
                )
            }
        } else {
            Column(
                modifier = Modifier.height(150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = items.textFirst),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(id = items.textSecond),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
