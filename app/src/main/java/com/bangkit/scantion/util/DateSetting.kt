package com.bangkit.scantion.util

import android.annotation.SuppressLint
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun getDayFormat(input: String): String{
    if (input.isEmpty()) return ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return LocalDateTime.parse(input,formatter ).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

fun millisecondsToDateString(milliseconds: Long, dateFormat: String = "d MMMM yyyy"): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds

    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    return sdf.format(calendar.time)
}

fun dateToMilliseconds(dateString: String, dateFormat: String = "d MMMM yyyy"): Long {
    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
    val date = sdf.parse(dateString)
    return date?.time ?: 0
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDate(
    snackScope: CoroutineScope,
    openDialog: MutableState<Boolean>,
    datePickerState: DatePickerState,
    confirmEnabled: State<Boolean>,
    onHowLongChange: (String) -> Unit
) {
    if (openDialog.value) {
        DatePickerDialog(onDismissRequest = {
            openDialog.value = false
        }, confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    snackScope.launch {
                        datePickerState.selectedDateMillis?.let {
                            millisecondsToDateString(
                                it
                            )
                        }?.let { onHowLongChange.invoke(it) }
                    }
                }, enabled = confirmEnabled.value
            ) {
                Text("OK")
            }
        }, dismissButton = {
            TextButton(onClick = {
                openDialog.value = false
            }) {
                Text("Cancel")
            }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}