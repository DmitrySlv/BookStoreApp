package com.dscreate_app.bookstoreapp.ui.add_book_scrren.models

data class Book(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val category: String = "",
    val imageUrl: String = "",
    val isFavourite: Boolean = false
)
