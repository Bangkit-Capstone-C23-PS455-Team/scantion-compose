package com.bangkit.scantion.presentation.examination

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.ComposeFileProvider


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
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var hasImage by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var uri = ComposeFileProvider.getImageUri(context)

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                photoUri = uri
                hasImage = true
                uri = ComposeFileProvider.getImageUri(context)
            }
        }
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uriResult ->
            if (photoUri != uriResult && uriResult != null) {
                photoUri = uriResult
                hasImage = true
            }
            Log.d("test uri", photoUri.toString())
        }
    )
    val saveState = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(items = items, index = pageState.currentPage, navController)

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth(),
            userScrollEnabled = false
        ) { page ->
            ExaminationItem(
                page,
                bodyPart,
                howLong,
                symptom,
                onBodyPartChange = { bodyPart = it },
                onSymptomChange = { symptom = it } ,
                onHowLongChange = { howLong = it },
                uri,
                photoUri,
                hasImage,
                singlePhotoPickerLauncher,
                takePictureLauncher
            )
        }

        val isQuestionAnswered = bodyPart.isNotEmpty() && howLong.isNotEmpty() && symptom.isNotEmpty()

        BottomSection(navController, hasImage, isQuestionAnswered, size = items.size, index = pageState.currentPage, onNextClick = {
            if (pageState.currentPage < items.size - 1) scope.launch {
                pageState.animateScrollToPage(pageState.currentPage + 1)
            }
        }) {
            if (pageState.currentPage + 1 > 1) scope.launch {
                pageState.animateScrollToPage(pageState.currentPage - 1)
            }
        }
    }
}

@Composable
fun ExaminationItem(
    page: Int,
    bodyPart: String,
    symptom: String,
    howLong: String,
    onBodyPartChange: (String) -> Unit,
    onSymptomChange: (String) -> Unit,
    onHowLongChange: (String) -> Unit,
    uri: Uri?,
    photoUri: Uri?,
    hasImage: Boolean,
    singlePhotoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    takePictureLauncher: ManagedActivityResultLauncher<Uri, Boolean>
) {
    when (page) {
        0 -> {
            AddPhotoPage(uri, photoUri, hasImage, singlePhotoPickerLauncher, takePictureLauncher)
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
            ResultPage()
        }
    }
}

@Composable
fun TopSection(items: List<ExaminationItems>, index: Int, navController: NavHostController) {
    val size = items.size
    val progress = (index.toFloat() + 1) / (items.size + 1)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

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
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                LinearProgressIndicator(
                    modifier = Modifier
                        .semantics(mergeDescendants = true) {}
                        .fillMaxWidth(),
                    progress = animatedProgress,
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
            // Indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(50.dp),
            ) {
                for (i in 0 until size){
                    Indicator(i, index, items[i])
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
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
fun Indicator(i: Int, index: Int, items: ExaminationItems) {
    val isDone = index >= i
    val boxColor = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val iconColor = if (isDone) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier.height(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .background(color = boxColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = items.icon,
                contentDescription = "icon indicator",
                tint = iconColor
            )
        }
        Text(
            text = items.pageName,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun BottomSection(
    navController: NavHostController,
    hasImage: Boolean,
    isQuestionAnswered: Boolean,
    size: Int,
    index: Int,
    onNextClick: () -> Unit = {},
    onPrevClick: () -> Unit = {}
) {
    val isLastPage = index == size - 1
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Previous Button
            Row(modifier = Modifier.fillMaxWidth(.5f), horizontalArrangement = Arrangement.Start) {
                AnimatedVisibility(index > 0, enter = fadeIn(), exit = fadeOut()) {
                    ScantionButton(
                        modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                        onClick = {
                            if (index > 0) {
                                if (!isLastPage){
                                    onPrevClick.invoke()
                                } else {
//                                    saveToPdf()
                                }
                            } },
                        text = if (isLastPage) "Simpan PDF" else "Kembali",
                        textStyle = MaterialTheme.typography.bodySmall,
                        outlineButton = true,
                        iconStart = !isLastPage,
                        iconEnd = isLastPage,
                        icon = if (isLastPage) Icons.Outlined.ArrowDropDown else Icons.Outlined.KeyboardArrowLeft
                    )
                }
            }

            val enabledNext =
                when (index) {
                    0 -> hasImage
                    1 -> isQuestionAnswered
                    else -> false
                }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ScantionButton(
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp),
                    enabled = if (isLastPage) true else enabledNext,
                    onClick = { if (isLastPage) navController.popBackStack() else onNextClick.invoke() },
                    text = if (isLastPage) "Selesai" else "Selanjutnya",
                    textStyle = MaterialTheme.typography.bodySmall,
                    iconEnd = true,
                    icon = Icons.Outlined.KeyboardArrowRight
                )
            }
        }
    }
}

fun saveToPdf() {
    TODO("Not yet implemented")
}

