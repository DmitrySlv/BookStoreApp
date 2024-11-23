package com.dscreate_app.bookstoreapp.add_book_scrren.data

import kotlinx.serialization.Serializable

@Serializable
data class AddScreenObj(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = ""
)