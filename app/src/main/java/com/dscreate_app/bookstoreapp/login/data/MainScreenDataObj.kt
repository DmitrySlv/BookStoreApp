package com.dscreate_app.bookstoreapp.login.data

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenDataObj(
    val uid: String = "",
    val email: String = ""
)
