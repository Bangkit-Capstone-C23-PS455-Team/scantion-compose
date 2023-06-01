package com.bangkit.scantion.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun TextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    text: String,
    textModifier: Modifier = Modifier.padding(5.dp),
    outlineButton: Boolean = false
) {
    if (outlineButton) {
        Button(
            modifier = modifier,
            shape = shape,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = textModifier,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        Button(
            modifier = modifier,
            shape = shape,
            onClick = onClick,
            enabled = enabled
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = textModifier
            )
        }
    }
}