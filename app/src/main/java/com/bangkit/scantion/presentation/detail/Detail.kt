package com.bangkit.scantion.presentation.detail

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.model.UserLog
import com.bangkit.scantion.navigation.Graph
import com.bangkit.scantion.presentation.examination.ResultPage
import com.bangkit.scantion.ui.component.ConfirmationDialog
import com.bangkit.scantion.ui.component.ScantionButton
import com.bangkit.scantion.util.Constants
import com.bangkit.scantion.util.ImageFileProvider
import com.bangkit.scantion.util.saveToPdf
import com.bangkit.scantion.viewmodel.ExaminationViewModel
import com.bangkit.scantion.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Detail(
    navController: NavHostController,
    skinCaseId: String,
    homeViewModel: HomeViewModel = hiltViewModel(),
    examinationViewModel: ExaminationViewModel = hiltViewModel()
){
    var userLog = UserLog()
    try {
        userLog = homeViewModel.userLog.value!!
    } catch (e: Exception){
        navController.popBackStack()
        navController.navigate(Graph.AUTHENTICATION)
    }

    val scope = rememberCoroutineScope()
    val skinCase = remember {
        mutableStateOf(Constants.skinCaseDetailPlaceHolder)
    }

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            skinCase.value = examinationViewModel.getSkinExamById(skinCaseId = skinCaseId) ?:Constants.skinCaseDetailPlaceHolder
        }
    }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween) {
        TopSection(navController = navController)
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.9f)) {
            ResultPage(navController, userLog = userLog, skinCase = skinCase.value, isFromDetail = true)
        }
        BottomSection(navController = navController, skinCase = skinCase.value, userLog = userLog, context = context, examinationViewModel)
    }
}

@Composable
fun TopSection(navController: NavController) {
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
                text = "Detail Pemeriksaan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BottomSection(
    navController: NavHostController,
    skinCase: SkinCase,
    userLog: UserLog,
    context: Context,
    examinationViewModel: ExaminationViewModel
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showDialog,
        title = "Apakah anda yakin ingin menghapus riwayat ini?",
        desc = "Data yang dihapus tidak dapat dikembalikan",
        confirmText = "Hapus",
        dismissText = "Batal",
        redAlert = true,
        onConfirm = {
            ImageFileProvider.deleteImageByUri(context, skinCase.id)
            examinationViewModel.deleteSkinExam(skinCase)
            navController.popBackStack()
        }
    )

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
                ScantionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    onClick = {
                        saveToPdf(context, skinCase, userLog.name)
                    },
                    text = "Simpan PDF",
                    textStyle = MaterialTheme.typography.bodySmall,
                    iconEnd = true,
                    icon = Icons.Outlined.ArrowDropDown,
                    outlineButton = true
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ScantionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    onClick = {showDialog.value = true},
                    text = "Hapus",
                    textStyle = MaterialTheme.typography.bodySmall,
                    iconEnd = true,
                    icon = Icons.Outlined.KeyboardArrowRight,
                    isDeleteButton = true
                )
            }
        }
    }
}