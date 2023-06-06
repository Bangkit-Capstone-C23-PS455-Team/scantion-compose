package com.bangkit.scantion.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun ScantionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    text: String,
    textStyle: TextStyle = LocalTextStyle.current,
    textModifier: Modifier = Modifier.padding(5.dp),
    outlineButton: Boolean = false,
    iconStart: Boolean = false,
    iconEnd: Boolean = false,
    icon: ImageVector? = null,
    isDeleteButton: Boolean = false
) {
    val itemColor =
        if (enabled) {
            if (outlineButton)
                MaterialTheme.colorScheme.onBackground
            else if (isDeleteButton)
                Color.White
            else
                MaterialTheme.colorScheme.onPrimary
        } else {
            Color.Unspecified
        }
    val buttonColor = if (outlineButton) ButtonDefaults.buttonColors(MaterialTheme.colorScheme.background) else if (isDeleteButton) ButtonDefaults.buttonColors(Color.Red) else ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
    val borderButton = if (outlineButton) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null

    Button(
        modifier = modifier,
        shape = shape,
        onClick = onClick,
        colors = buttonColor,
        border = borderButton,
        enabled = enabled
    ) {
        Box (modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = text,
                    style = textStyle,
                    modifier = textModifier,
                    color = itemColor,
                )
            }
            if (icon != null) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = if (iconStart) Alignment.CenterStart else if (iconEnd) Alignment.CenterEnd else Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = itemColor
                    )
                }
            }
        }
    }
}