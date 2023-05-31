package com.whyaji.scantion.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


class ExaminationItems (
    val pageName: String,
    val hint: String,
    val icon: ImageVector
) {
    companion object {
        fun getData(): List<ExaminationItems> {
            return listOf(
                ExaminationItems(
                    "photo",
                    "Tambahkan foto kulit anda yang ingin diperiksa. \n" +
                            "Pastikan gambar masalah kulit anda terlihat dan berada di tengah.",
                    Icons.Outlined.Add
                ),
                ExaminationItems(
                    "question",
                    "Jawablah beberapa pertanyaan berikut.",
                    Icons.Outlined.Info
                ),
                ExaminationItems(
                    "result",
                    "Ini lah hasil dari pemeriksaan masalah kulit anda.",
                    Icons.Outlined.Check
                )
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Examination(navController: NavHostController) {
    val items = ExaminationItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(items = items, index = pageState.currentPage, navController)

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            ExaminationItem(items = items[page], page, navController)
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
private fun ExaminationItem(items: ExaminationItems, page: Int, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Text(text = items.hint,
        modifier = Modifier.padding(top = 100.dp))
    }
}

@Composable
private fun TopSection(items: List<ExaminationItems>, index: Int, navController: NavHostController) {
    val size = items.size
    Column() {
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
                    imageVector = Icons.Outlined.Close, contentDescription = "close"
                )
            }
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Pemeriksaan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant),
            )
            // Indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(50.dp),
            ) {
                for (i in 0 until size){
                    Indicator(isSelected = i == index, items[i])
                }
            }
        }
    }
}

@Composable
private fun Indicator(isSelected: Boolean, items: ExaminationItems) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(60.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = items.icon, contentDescription = "icon indicator"
        )
    }
}

@Composable
private fun BottomSection(
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

