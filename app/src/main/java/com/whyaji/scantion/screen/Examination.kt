package com.whyaji.scantion.screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.whyaji.scantion.ui.component.AuthSpacer
import com.whyaji.scantion.ui.component.TextFieldQuestion


class ExaminationItems (
    val pageName: String,
    val hint: String,
    val icon: ImageVector
) {
    companion object {
        fun getData(): List<ExaminationItems> {
            return listOf(
                ExaminationItems(
                    "Foto",
                    "Tambahkan foto kulit anda yang ingin diperiksa.",
                    Icons.Outlined.Add
                ),
                ExaminationItems(
                    "Pertanyaan",
                    "Jawablah beberapa pertanyaan berikut.",
                    Icons.Outlined.Info
                ),
                ExaminationItems(
                    "Hasil",
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

    var bodyPart by rememberSaveable { mutableStateOf("") }
    var howLong by rememberSaveable { mutableStateOf("") }
    var symptom by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(items = items, index = pageState.currentPage, navController)

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            ExaminationItem(
                page,
                navController,
                bodyPart,
                howLong,
                symptom,
                onBodyPartChange = { bodyPart = it },
                onSymptomChange = { symptom = it } ,
                onHowLongChange = { howLong = it }
            )
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
private fun ExaminationItem(
    page: Int,
    navController: NavHostController,
    bodyPart: String,
    symptom: String,
    howLong: String,
    onBodyPartChange: (String) -> Unit,
    onSymptomChange: (String) -> Unit,
    onHowLongChange: (String) -> Unit
) {
    when (page) {
        0 -> {

        }
        1 -> {
            QuestionPage(
                bodyPart,
                howLong,
                symptom,
                onBodyPartChange,
                onSymptomChange,
                onHowLongChange
            )
        }
        2 -> {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestionPage(
    bodyPart: String,
    symptom: String,
    howLong: String,
    onBodyPartChange: (String) -> Unit,
    onSymptomChange: (String) -> Unit,
    onHowLongChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextFieldQuestion(
            text = "Bagian kulit mana yang diperiksa?",
            placeholder = "Bagian Kulit (misal: Tangan)",
            value = bodyPart,
            onChangeValue = onBodyPartChange
        )
        TextFieldQuestion(
            text = "Sudah berapa lama masalah kulit ini muncul?",
            placeholder = "Berapa lama (misal: 1 tahun 2 bulan)",
            value = symptom,
            onChangeValue = onSymptomChange
        )
        TextFieldQuestion(
            text = "Apa saja gejala kulit yang anda alami?",
            placeholder = "Gejala (misal: gatal, panas, kering)",
            value = howLong,
            onChangeValue = onHowLongChange
        )
    }
}

@Composable
private fun TopSection(items: List<ExaminationItems>, index: Int, navController: NavHostController) {
    val size = items.size
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
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = MaterialTheme.colorScheme.surfaceVariant),
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
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
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = items[index].hint,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun Indicator(isSelected: Boolean, items: ExaminationItems) {
    val colorPrimary = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val colorSurfaceVariant = if (!isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    Column(
        modifier = Modifier.height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .background(
                    color = colorPrimary
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = items.icon,
                contentDescription = "icon indicator",
                tint = colorSurfaceVariant
            )
        }
        Text(
            text = items.pageName,
            style = MaterialTheme.typography.bodyMedium,
            color = colorPrimary,
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

