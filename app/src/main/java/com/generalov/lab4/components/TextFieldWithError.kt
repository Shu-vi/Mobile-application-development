package com.generalov.lab4.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldWithError(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String,
    modifier: Modifier,
    isPassword: Boolean = false
) {
    val isError = errorMessage.isNotEmpty()
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    Column {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            isError = isError,
            label = { Text(label) },
            modifier = modifier,
            visualTransformation = visualTransformation
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}
