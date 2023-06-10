package com.bangkit.scantion.presentation.explanation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bangkit.scantion.model.CancerType
import com.bangkit.scantion.presentation.examination.ColumnPartResult
import com.bangkit.scantion.presentation.examination.ResultSpacer
import com.bangkit.scantion.presentation.examination.TitleTextResult

@Composable
fun Explanation(navController: NavHostController, cancerKey: String){
    val cancerTypes = CancerType.getData()
    var cancerType: CancerType? = null

    if (cancerKey in CancerType.listKey){
        cancerType = cancerTypes.getValue(cancerKey)
    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween) {
        TopSection(navController = navController)
        if (cancerType != null) ContentSection(cancerType)
    }
}

@Composable
fun ContentSection(cancerType: CancerType) {
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        ColumnPartResult {
            TitleTextResult(text = cancerType.fullName)
            Text(text = cancerType.desc)
        }
        if (cancerType.symptom.isNotEmpty()){
            ResultSpacer()
            ColumnPartResult {
                TitleTextResult(text = "Symptoms")
                Text(text = cancerType.symptom)
            }
        }
        if (cancerType.type.isNotEmpty()){
            ResultSpacer()
            ColumnPartResult {
                TitleTextResult(text = "Type of these lesions")
                Text(text = cancerType.type)
            }
        }
        if (cancerType.note.isNotEmpty()){
            ResultSpacer()
            ColumnPartResult {
                Text(text = cancerType.note)
            }
        }
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
                text = "Penjelasan Indikasi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}