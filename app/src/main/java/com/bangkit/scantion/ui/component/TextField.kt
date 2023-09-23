package com.bangkit.scantion.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.bangkit.scantion.R

@Composable
fun TextFieldQuestion(
    readOnly: Boolean = false,
    modifier: Modifier,
    text: String,
    placeholder: String = "",
    value: String,
    onChangeValue: (String) -> Unit,
    nextFocusRequester: FocusRequester? = null,
    isLast: Boolean = false,
    performAction: () -> Unit = {}
) {
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next
    val keyboardActions = KeyboardActions(
        onNext = {
            nextFocusRequester?.requestFocus()
        },
        onDone = { performAction.invoke() }
    )
    val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text, imeAction = imeAction
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            readOnly = readOnly,
            modifier = modifier,
            value = value,
            onValueChange = onChangeValue,
            placeholder = { Text(text = placeholder, color = MaterialTheme.colorScheme.secondary) },
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions
        )
    }
}

@Composable
fun AuthTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordTf: Boolean = false,
    isEmailTf: Boolean = false,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    visibility: MutableState<Boolean> = rememberSaveable { mutableStateOf(true) },
    nextFocusRequester: FocusRequester? = null,
    isLast: Boolean = false,
    buttonEnabled: Boolean = false,
    performAction: () -> Unit = {}
) {
    val imeAction = if (isLast) ImeAction.Done else ImeAction.Next
    val keyboardActions = KeyboardActions(
        onNext = {
            nextFocusRequester?.requestFocus()
        },
        onDone = { if (buttonEnabled) performAction.invoke() }
    )
    val keyboardOptions = KeyboardOptions(
        keyboardType = if (isPasswordTf) KeyboardType.Password else if (isEmailTf) KeyboardType.Email else KeyboardType.Text,
        imeAction = imeAction
    )

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = label,
        maxLines = 1,
        visualTransformation = if (isPasswordTf && visibility.value) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIcon,
        trailingIcon = if (!isPasswordTf) null else {
            {
                val imageVisibility = ImageVector.vectorResource(id = R.drawable.ic_visibility)
                val imageVisibilityOff =
                    ImageVector.vectorResource(id = R.drawable.ic_visibility_off)

                val description = if (visibility.value) "Hide password" else "Show password"

                IconButton(
                    onClick = { visibility.value = !visibility.value },
                ) {
                    Icon(
                        imageVector = if (visibility.value) imageVisibility else imageVisibilityOff,
                        contentDescription = description
                    )
                }
            }
        },
    )
}