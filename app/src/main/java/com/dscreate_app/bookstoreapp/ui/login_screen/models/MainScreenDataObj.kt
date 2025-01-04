package com.dscreate_app.bookstoreapp.ui.login_screen.models

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenDataObj(
    val uid: String = "",
    val email: String = ""
)
