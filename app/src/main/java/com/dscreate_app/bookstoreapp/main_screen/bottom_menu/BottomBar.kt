package com.dscreate_app.bookstoreapp.main_screen.bottom_menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource

@Composable
fun BottomBar(
    onFavouritesClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    val items = listOf(
        BottomBarItem.Home,
        BottomBarItem.Favourite,
        BottomBarItem.Settings,
    )

    val selectedItemState = remember {
        mutableStateOf("Home")
    }

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = selectedItemState.value == item.title, //сравнивает элемент из списка и делает true при совпадении
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = item.title)
                },
                onClick = {
                    selectedItemState.value = item.title
                    when(item.title) {
                        BottomBarItem.Home.title -> onHomeClick()
                        BottomBarItem.Favourite.title -> onFavouritesClick()
                    }
                }
            )
        }

    }
}