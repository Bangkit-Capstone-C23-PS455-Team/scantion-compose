package com.bangkit.scantion.presentation.examination

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import com.bangkit.scantion.R
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.ComposeFileProvider
import com.bangkit.scantion.util.checkPermissions
import com.bangkit.scantion.util.requestPermission
import com.bangkit.scantion.util.saveToPdf


class ExaminationItems(
    val pageName: String,
    val hint: String,
    val icon: ImageVector
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Examination(navController: NavHostController) {
    val items = listOf(
        ExaminationItems(
            "Foto",
            "Tambahkan foto kulit anda yang ingin diperiksa.",
            ImageVector.vectorResource(id = R.drawable.ic_add_photo)
        ),
        ExaminationItems(
            "Pertanyaan",
            "Jawablah beberapa pertanyaan berikut.",
            ImageVector.vectorResource(id = R.drawable.ic_question)
        ),
        ExaminationItems(
            "Hasil",
            "Ini lah hasil dari pemeriksaan masalah kulit anda.",
            ImageVector.vectorResource(id = R.drawable.ic_result)
        )
    )
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    var bodyPart by rememberSaveable { mutableStateOf("") }
    var howLong by rememberSaveable { mutableStateOf("") }
    var symptom by rememberSaveable { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var hasImage by remember { mutableStateOf(false) }

    var isProcessDone by remember { mutableStateOf(false) }
    if (pageState.currentPage == items.size - 1) isProcessDone = true

    val context = LocalContext.current
    var uri = ComposeFileProvider.getImageUri(context)

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = uri
            hasImage = true
            uri = ComposeFileProvider.getImageUri(context)
        }
    }

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

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(items = items, index = pageState.currentPage, navController)

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(.89f)
                .fillMaxWidth(),
            userScrollEnabled = false
        ) { page ->
            ExaminationItem(
                page,
                bodyPart,
                howLong,
                symptom,
                onBodyPartChange = { bodyPart = it },
                onSymptomChange = { symptom = it },
                onHowLongChange = { howLong = it },
                uri,
                photoUri,
                hasImage,
                singlePhotoPickerLauncher,
                takePictureLauncher,
                isProcessDone
            )
        }

        val isQuestionAnswered =
            bodyPart.isNotEmpty() && howLong.isNotEmpty() && symptom.isNotEmpty()

        BottomSection(
            navController,
            bodyPart,
            howLong,
            symptom,
            photoUri,
            hasImage,
            isQuestionAnswered,
            size = items.size,
            index = pageState.currentPage,
            onNextClick = {
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
    takePictureLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    isProcessDone: Boolean = false
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
            if (isProcessDone) {
                ResultPage(
                    SkinCase(
                        photoUri = photoUri.toString(),
                        bodyPart = bodyPart,
                        howLong = howLong,
                        symptom = symptom,
                        cancerType = "Melanoma",
                        accuracy = .86f
                    )
                )
            }
        }
    }
}

@Composable
fun TopSection(items: List<ExaminationItems>, index: Int, navController: NavHostController) {
    val size = items.size
    val progress = if (index == size - 1) (size + 1).toFloat() else (index.toFloat() + 1) / (items.size + 1)
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
                for (i in 0 until size) {
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
    val boxColor =
        if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val iconColor =
        if (isDone) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary
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
                tint = iconColor,
                modifier = Modifier
                    .width(32.dp)
                    .aspectRatio(1f)
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
    bodyPart: String,
    symptom: String,
    howLong: String,
    photoUri: Uri?,
    hasImage: Boolean,
    isQuestionAnswered: Boolean,
    size: Int,
    index: Int,
    onNextClick: () -> Unit = {},
    onPrevClick: () -> Unit = {}
) {
    val isOnResult = index == size - 1
    val isLastInput = index == size - 2

    val ctx = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            // Previous Button
            Row(modifier = Modifier.fillMaxWidth(.5f), horizontalArrangement = Arrangement.Start) {
                AnimatedVisibility(index > 0, enter = fadeIn(), exit = fadeOut()) {
                    ScantionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        onClick = {
                            if (index > 0) {
                                if (!isOnResult) {
                                    onPrevClick.invoke()
                                } else {
                                    if (checkPermissions(ctx)) {
                                        Toast.makeText(ctx, "Permissions Granted..", Toast.LENGTH_SHORT).show()
                                    } else {
                                        requestPermission(activity!!)
                                    }
                                    saveToPdf(ctx, SkinCase(
                                        photoUri = photoUri.toString(),
                                        bodyPart = bodyPart,
                                        howLong = howLong,
                                        symptom = symptom,
                                        cancerType = "Melanoma",
                                        accuracy = .86f
                                    )
                                    )
                                }
                            }
                        },
                        text = if (isOnResult) "Simpan PDF" else "Kembali",
                        textStyle = MaterialTheme.typography.bodySmall,
                        outlineButton = true,
                        iconStart = !isOnResult,
                        iconEnd = isOnResult,
                        icon = if (isOnResult) Icons.Outlined.ArrowDropDown else Icons.Outlined.KeyboardArrowLeft
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    enabled = if (isOnResult) true else enabledNext,
                    onClick = {
                        if (isOnResult) navController.popBackStack() else onNextClick.invoke()
                    },
                    text = if (isOnResult) "Selesai" else if (isLastInput) "Proses" else "Selanjutnya",
                    textStyle = MaterialTheme.typography.bodySmall,
                    iconEnd = true,
                    icon = Icons.Outlined.KeyboardArrowRight
                )
            }
        }
    }
}
