package com.dscreate_app.bookstoreapp.ui.details_screen.models

import kotlinx.serialization.Serializable

@Serializable
data class DetailsNavObj(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = "",
)