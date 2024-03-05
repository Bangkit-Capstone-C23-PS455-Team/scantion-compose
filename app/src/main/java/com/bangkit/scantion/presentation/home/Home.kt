package com.bangkit.scantion.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bangkit.scantion.navigation.HomeScreen
import com.bangkit.scantion.R
import com.bangkit.scantion.model.News
import com.bangkit.scantion.model.SkinCase
import com.bangkit.scantion.model.UserLog
import com.bangkit.scantion.navigation.Graph
import com.bangkit.scantion.presentation.history.SkinCaseListItem
import com.bangkit.scantion.ui.component.CarouselNews
import com.bangkit.scantion.util.Constants.orPlaceHolderList
import com.bangkit.scantion.viewmodel.AuthViewModel
import com.bangkit.scantion.viewmodel.ExaminationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Home(
    navController: NavHostController,
    viewModel: AuthViewModel = hiltViewModel(),
    examinationViewModel: ExaminationViewModel = hiltViewModel()
) {
    var userLog = UserLog()

    viewModel.currentUser.let {
        if (it != null) {
            userLog = UserLog(it.uid, it.displayName.toString(), it.email.toString())
        } else {
            navController.popBackStack()
            navController.navigate(Graph.AUTHENTICATION)
        }
    }

    val newsList = News.getData()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Halo, ${userLog.name}",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
        Card(
            shape = MaterialTheme.shapes.large, modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(start = 16.dp, top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Ingin periksa kulit anda?",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Periksa sekarang",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Button(onClick = {
                            navController.navigate(HomeScreen.Examination.route)
                        }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.width(86.dp)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_examination),
                                    contentDescription = "Icon Add",
                                    modifier = Modifier
                                        .width(30.dp)
                                        .aspectRatio(1f)
                                )
                                Text(text = "Periksa")
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.img_card_examination),
                        contentDescription = "image examination illustration"
                    )
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)){
            CarouselNews(navController = navController, newsList = newsList)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Riwayat Pemeriksaan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = "Lihat semua",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    navController.navigate(HomeScreen.History.route)
                })
        }
        Column(modifier = Modifier.fillMaxSize()) {
            LastExam(navController, examinationViewModel, userLog.id)
        }
    }
}

@Composable
fun LastExam(navController: NavHostController, examinationViewModel: ExaminationViewModel, userId: String) {
    val skinExams = examinationViewModel.skinExams.observeAsState()
    val total = if (skinExams.value.orPlaceHolderList(userId).size > 2) 2 else skinExams.value.orPlaceHolderList(userId).size
    LastSkinExams(navController = navController, skinCases = skinExams.value.orPlaceHolderList(userId), total)
}

@Composable
fun LastSkinExams(navController: NavHostController, skinCases: List<SkinCase>, total: Int) {
    if (skinCases[0].id == "empty"){
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = skinCases[0].userId)
            Text(text = skinCases[0].bodyPart)
        }
    } else if (total >= 1){
        for (i in 0 until total){
            SkinCaseListItem(
                skinCases[i],
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
