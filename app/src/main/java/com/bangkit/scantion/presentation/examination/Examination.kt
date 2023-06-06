package com.bangkit.scantion.presentation.examination

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bangkit.scantion.R
import com.bangkit.scantion.ScantionApp
import com.bangkit.scantion.model.ExaminationItems
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.model.UserLog
import com.bangkit.scantion.navigation.Graph
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.ImageFileProvider
import com.bangkit.scantion.util.ImageFileProvider.Companion.savedImage
import com.bangkit.scantion.util.checkStoragePermissions
import com.bangkit.scantion.util.requestStoragePermissions
import com.bangkit.scantion.util.saveToPdf
import com.bangkit.scantion.viewmodel.ExaminationViewModel
import com.bangkit.scantion.viewmodel.HomeViewModel
import com.bangkit.scantion.viewmodel.ViewModelFactory

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun Examination(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    examinationViewModel: ExaminationViewModel = viewModel(
        factory = ViewModelFactory(ScantionApp.getInstance().getDb().SkinExamsDao())
    )
) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)

    var userLog = UserLog()

    try {
        userLog = homeViewModel.userLog.value!!
    } catch (e: Exception){
        navController.popBackStack()
        navController.popBackStack()
        navController.navigate(Graph.AUTHENTICATION)
    }

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
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var hasImage by rememberSaveable { mutableStateOf(false) }
    var isProcessDone by rememberSaveable { mutableStateOf(false) }

    var skinCase by rememberSaveable { mutableStateOf<SkinCase?>(null) }

    if (isProcessDone && skinCase == null){
        skinCase = SkinCase(
            userId = userLog.id,
            photoUri = photoUri.toString(),
            bodyPart = bodyPart,
            howLong = howLong,
            symptom = symptom,
            cancerType = "Melanoma",
            accuracy = .86f
        )
        examinationViewModel.addSkinExam(skinCase!!)
    }

    val showDialog = rememberSaveable { mutableStateOf(false) }
    val backCallbackEnabled = rememberSaveable { mutableStateOf(false) }

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!isProcessDone) {
                    showDialog.value = true
                } else {
                    navController.popBackStack()
                }
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    DisposableEffect(dispatcher, backCallbackEnabled.value, lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (backCallbackEnabled.value) {
                        dispatcher?.addCallback(backCallback)
                    }
                }
                Lifecycle.Event.ON_STOP -> backCallback.remove()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            backCallback.remove()
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    ExitConfirmationDialog(
        showDialog = showDialog,
        onConfirmExit = {
            ImageFileProvider.deleteImageUnused(context)
            navController.popBackStack()
        },
        onDismiss = { showDialog.value = false }
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(items = items, index = pageState.currentPage, navController, isProcessDone, hasImage, showDialog)

        HorizontalPager(
            count = items.size,
            state = pageState,
            modifier = Modifier
                .fillMaxHeight(.89f)
                .fillMaxWidth(),
            userScrollEnabled = false
        ) { page ->
            ExaminationPage(
                context,
                userLog,
                page,
                bodyPart,
                howLong,
                symptom,
                photoUri,
                hasImage,
                isProcessDone,
                onBodyPartChange = { bodyPart = it },
                onSymptomChange = { symptom = it },
                onHowLongChange = { howLong = it },
                onPhotoUriChange = { photoUri = it },
                onHasImageChange = { hasImage = it },
                backCallbackEnabled,
                skinCase
            )
        }

        val isQuestionAnswered =
            bodyPart.isNotEmpty() && howLong.isNotEmpty() && symptom.isNotEmpty()

        if (activity != null) {
            BottomSection(
                userLog,
                activity,
                context,
                navController,
                hasImage,
                isQuestionAnswered,
                skinCase,
                items.size,
                pageState.currentPage,
                onNextClick = {
                    if (pageState.currentPage < items.size - 1) scope.launch {
                        pageState.animateScrollToPage(pageState.currentPage + 1)
                    }
                },
                onPrevClick = {
                    if (pageState.currentPage + 1 > 1) scope.launch {
                        pageState.animateScrollToPage(pageState.currentPage - 1)
                    }
                },
                onProcessClick = {
                    photoUri = savedImage(context, photoUri!!)
                    isProcessDone = true
                }
            )
        }
    }
}

@Composable
fun ExaminationPage(
    context: Context,
    userLog: UserLog,
    page: Int,
    bodyPart: String,
    symptom: String,
    howLong: String,
    photoUri: Uri?,
    hasImage: Boolean,
    isProcessDone: Boolean = false,
    onBodyPartChange: (String) -> Unit,
    onSymptomChange: (String) -> Unit,
    onHowLongChange: (String) -> Unit,
    onPhotoUriChange: (Uri) -> Unit,
    onHasImageChange: (Boolean) -> Unit,
    backCallbackEnabled: MutableState<Boolean>,
    skinCase: SkinCase?
) {
    when (page) {
        0 -> {
            AddPhotoPage(
                context,
                photoUri,
                hasImage,
                onPhotoUriChange,
                onHasImageChange,
                backCallbackEnabled
            )
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
            if (isProcessDone && skinCase != null) {
                ResultPage(
                    userLog,
                    skinCase
                )
            }
        }
    }
}

@Composable
fun TopSection(
    items: List<ExaminationItems>,
    index: Int,
    navController: NavHostController,
    isProcessDone: Boolean,
    hasImage: Boolean,
    showDialog: MutableState<Boolean>
) {
    val size = items.size
    val progress =
        if (index == size - 1) (size + 1).toFloat() else (index.toFloat() + 1) / (items.size + 1)
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
                    if (!hasImage || isProcessDone){
                        navController.popBackStack()
                    } else {
                        showDialog.value = true
                    }
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
    userLog: UserLog,
    activity: Activity,
    context: Context,
    navController: NavHostController,
    hasImage: Boolean,
    isQuestionAnswered: Boolean,
    skinCase: SkinCase?,
    size: Int,
    index: Int,
    onNextClick: () -> Unit = {},
    onPrevClick: () -> Unit = {},
    onProcessClick: () -> Unit = {},
) {
    val isOnResult = index == size - 1
    val isLastInput = index == size - 2

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
                                    if (checkStoragePermissions(context)) {
                                        Toast.makeText(
                                            context,
                                            "Permissions Granted..",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (skinCase != null) {
                                            saveToPdf(context, skinCase, userLog.name)
                                        }
                                    } else {
                                        requestStoragePermissions(activity)
                                    }
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
                        if (isLastInput) onProcessClick.invoke()
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

@Composable
fun ExitConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                onDismiss()
            },
//            title = { Text(text = stringResource(id = R.string.exit_confirmation_title)) },
//            text = { Text(text = stringResource(id = R.string.exit_confirmation_message)) },
            title = { Text(text = "Apakah anda yakin keluar dari halaman pemeriksaan?") },
            text = { Text(text = "Data yang sudah anda masukan akan dihapus") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        onConfirmExit()
                    }
                ) {
//                    Text(text = stringResource(id = R.string.exit_confirmation_confirm))
                    Text(text = "Keluar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        onDismiss()
                    }
                ) {
//                    Text(text = stringResource(id = R.string.exit_confirmation_cancel))
                    Text(text = "Batal")
                }
            }
        )
    }
}