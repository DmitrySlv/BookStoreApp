package com.dscreate_app.bookstoreapp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dscreate_app.bookstoreapp.ui.theme.ButtonColorPurple

@Composable
fun LoginButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(0.5f),
        colors = ButtonDefaults.buttonColors(
            containerColor = ButtonColorPurple
        ),
        onClick = { onClick() }
    ) {
        Text(
            text = text
        )
    }
}