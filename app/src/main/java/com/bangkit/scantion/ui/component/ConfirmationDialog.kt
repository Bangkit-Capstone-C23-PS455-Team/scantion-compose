package com.bangkit.scantion.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color

@Composable
fun ConfirmationDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    desc: String,
    confirmText: String,
    dismissText: String,
    redAlert: Boolean = false,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
                onDismiss()
            },
            title = { Text(text = title) },
            text = { Text(text = desc) },
            confirmButton = {
                Button(
                    colors = if (redAlert) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(),
                    onClick = {
                        showDialog.value = false
                        onConfirm()
                    },
                ) {
                    Text(
                        text = confirmText,
                        color = if (redAlert) Color.White else Color.Unspecified
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        onDismiss()
                    }
                ) {
                    Text(text = dismissText)
                }
            }
        )
    }
}