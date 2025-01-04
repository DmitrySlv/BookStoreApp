package com.dscreate_app.bookstoreapp.ui.main_screen.bottom_menu

import com.dscreate_app.bookstoreapp.R

sealed class BottomBarItem(
    val route: String,
    val title: String,
    val iconId: Int,
) {

    data object Home: BottomBarItem(
        route = "",
        title = "Главная",
        iconId = R.drawable.ic_home
    )

    data object Favourite: BottomBarItem(
        route = "",
        title = "Избранное",
        iconId = R.drawable.ic_favorite
    )

    data object Settings: BottomBarItem(
        route = "",
        title = "Настройки",
        iconId = R.drawable.ic_settings
    )
}