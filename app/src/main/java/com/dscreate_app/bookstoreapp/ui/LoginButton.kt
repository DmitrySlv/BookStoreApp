package com.dscreate_app.bookstoreapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.dscreate_app.bookstoreapp.ui.theme.ButtonColorPurpleLight

@Composable
fun LoginButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(0.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColorPurpleLight
        ),
        onClick = { onClick() }
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text
        )
    }
}