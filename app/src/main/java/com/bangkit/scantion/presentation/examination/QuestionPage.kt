package com.bangkit.scantion.presentation.examination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bangkit.scantion.ui.component.TextFieldQuestion

@Composable
fun QuestionPage(
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
            value = howLong,
            onChangeValue = onHowLongChange
        )
        TextFieldQuestion(
            text = "Apa saja gejala kulit yang anda alami?",
            placeholder = "Gejala (misal: gatal, panas, kering)",
            value = symptom,
            onChangeValue = onSymptomChange
        )
    }
}