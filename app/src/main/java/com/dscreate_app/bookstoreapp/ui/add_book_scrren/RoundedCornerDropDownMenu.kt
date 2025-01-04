package com.dscreate_app.bookstoreapp.ui.add_book_scrren

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dscreate_app.bookstoreapp.ui.theme.ButtonColor

@Composable
fun RoundedCornerDropDownMenu(
    defCategory: String,
    onOptionSelected: (String) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    val selectedOption = remember { mutableStateOf(defCategory) }

    val categoriesList = listOf(
        "Фэнтэзи",
        "Драма",
        "Бестселлеры"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = ButtonColor,
                shape = RoundedCornerShape(25.dp)
            )
            .clip(RoundedCornerShape(25.dp))
            .clickable {
                expanded.value = true
            }
            .background(Color.White)
            .padding(15.dp)

    ) {
        Text(text = selectedOption.value)
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            categoriesList.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option)
                    },
                    onClick = {
                        selectedOption.value = option
                        expanded.value = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}