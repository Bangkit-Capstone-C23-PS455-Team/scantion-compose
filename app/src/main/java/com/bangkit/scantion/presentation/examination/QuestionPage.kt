package com.bangkit.scantion.presentation.examination

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.bangkit.scantion.ui.component.TextFieldQuestion
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import com.bangkit.scantion.util.DialogDate
import com.bangkit.scantion.util.dateToMilliseconds


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionPage(
    bodyPart: String,
    symptom: String,
    howLong: String,
    onBodyPartChange: (String) -> Unit,
    onSymptomChange: (String) -> Unit,
    onHowLongChange: (String) -> Unit,
) {
    val bodyPartFocusRequester = remember { FocusRequester() }
    val symptomFocusRequester = remember { FocusRequester() }

    val snackScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (howLong.isNotEmpty()) dateToMilliseconds(howLong) else null
    )
    val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }

    val focusManager = LocalFocusManager.current
    DialogDate(snackScope, openDialog, datePickerState, confirmEnabled, onHowLongChange)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextFieldQuestion(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(bodyPartFocusRequester),
            text = "Bagian kulit mana yang diperiksa?",
            placeholder = "Bagian Kulit (misal: Tangan)",
            value = bodyPart,
            onChangeValue = onBodyPartChange,
            nextFocusRequester = symptomFocusRequester
        )
        TextFieldQuestion(modifier = Modifier
            .fillMaxWidth()
            .focusRequester(symptomFocusRequester),
            text = "Apa saja gejala kulit yang anda alami?",
            placeholder = "Gejala (misal: gatal, panas, kering)",
            value = symptom,
            onChangeValue = onSymptomChange,
            isLast = true,
            performAction = { focusManager.clearFocus() })

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Sejak kapan masalah kulit ini muncul?")
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                onClick = {
                    focusManager.clearFocus()
                    openDialog.value = true
                }, shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = howLong.ifEmpty { "Pilih tanggal" },
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(imageVector = Icons.Outlined.DateRange, contentDescription = "datePicker")
                }
            }
        }
    }
}