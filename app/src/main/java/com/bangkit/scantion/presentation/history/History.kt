package com.bangkit.scantion.presentation.history

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.bangkit.scantion.ScantionApp
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.navigation.HomeScreen
import com.bangkit.scantion.util.Constants.orPlaceHolderList
import com.bangkit.scantion.util.getDayFormat
import com.bangkit.scantion.viewmodel.ExaminationViewModel
import com.bangkit.scantion.viewmodel.ViewModelFactory

@Composable
fun History(
    navController: NavHostController,
    examinationViewModel: ExaminationViewModel = viewModel(
        factory = ViewModelFactory(ScantionApp.getInstance().getDb().SkinExamsDao())
    )
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopSection(navController)
        ContentSection(navController, examinationViewModel)
    }
}

@Composable
fun ContentSection(
    navController: NavController,
    examinationViewModel: ExaminationViewModel
) {
    val skinCaseQuery = remember { mutableStateOf("") }
    val skinExams = examinationViewModel.skinExams.observeAsState()
    ListSkinExams(
        navController = navController,
        skinCases = skinExams.value.orPlaceHolderList(),
        query = skinCaseQuery
    )
}

@Composable
fun ListSkinExams(
    navController: NavController,
    skinCases: List<SkinCase>,
    query: MutableState<String>,
) {
    if (skinCases[0].id == "empty") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = skinCases[0].userId)
            Text(text = skinCases[0].bodyPart)
        }
    } else {
        LazyColumn {
            val queriedSkinExams = if (query.value.isEmpty()) {
                skinCases
            } else {
                skinCases.filter { it.id.contains(query.value) || it.bodyPart.contains(query.value) }
            }
            itemsIndexed(queriedSkinExams) { _, skinCase ->
                SkinCaseListItem(
                    skinCase,
                    navController = navController
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SkinCaseListItem(skinCase: SkinCase, navController: NavController) {
    return Box(
        modifier = Modifier
            .height(120.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth()
                .height(120.dp)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        if (skinCase.id.isNotEmpty()) {
                            navController.navigate(HomeScreen.Detail.createRoute(skinCase.id))
                        }
                    },
                )

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (skinCase.photoUri.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = Uri.parse(skinCase.photoUri))
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.3f)
                            .fillMaxHeight(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)) {
                    Text(
                        text = if (skinCase.cancerType == "Normal") "Aman" else "Terindikasi",
                        color = if (skinCase.cancerType == "Normal") Color.Green else Color.Red
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = skinCase.bodyPart,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${skinCase.cancerType} ${(skinCase.accuracy * 100).toInt()}%",
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = getDayFormat(skinCase.dateCreated),
                    )
                }
            }

        }
    }
}

@Composable
fun TopSection(navController: NavController) {
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
            text = "Riwayat Pemeriksaan",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}