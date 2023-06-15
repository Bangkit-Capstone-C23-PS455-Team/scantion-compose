package com.bangkit.scantion.presentation.explanation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bangkit.scantion.model.CancerType
import com.bangkit.scantion.presentation.examination.ColumnPartResult
import com.bangkit.scantion.presentation.examination.ResultSpacer
import com.bangkit.scantion.presentation.examination.TitleTextResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Explanation(navController: NavHostController, cancerKey: String) {
    val cancerTypes = CancerType.getData()
    var cancerType: CancerType? = null

    if (cancerKey in CancerType.listKey) {
        cancerType = cancerTypes.getValue(cancerKey)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Penjelasan Indikasi",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowLeft,
                            contentDescription = "back"
                        )
                    }
                },
            )
        },
        content = {innerPadding ->
            if (cancerType != null) {
                LazyColumn(
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = {
                    item{
                        ColumnPartResult {
                            TitleTextResult(text = cancerType.fullName)
                            Text(text = cancerType.desc)
                        }
                        if (cancerType.symptom.isNotEmpty()) {
                            ResultSpacer()
                            ColumnPartResult {
                                TitleTextResult(text = "Symptoms")
                                Text(text = cancerType.symptom)
                            }
                        }
                        if (cancerType.type.isNotEmpty()) {
                            ResultSpacer()
                            ColumnPartResult {
                                TitleTextResult(text = "Type of these lesions")
                                Text(text = cancerType.type)
                            }
                        }
                        if (cancerType.note.isNotEmpty()) {
                            ResultSpacer()
                            ColumnPartResult {
                                Text(text = cancerType.note)
                            }
                        }
                    }
                })
            }
        }
    )
}