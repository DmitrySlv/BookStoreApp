package com.dscreate_app.bookstoreapp.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.dscreate_app.bookstoreapp.add_book_scrren.models.Book
import com.dscreate_app.bookstoreapp.add_book_scrren.models.Favourite
import com.dscreate_app.bookstoreapp.login.data.MainScreenDataObj
import com.dscreate_app.bookstoreapp.main_screen.bottom_menu.BottomBar
import com.dscreate_app.bookstoreapp.main_screen.bottom_menu.BottomBarItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navData: MainScreenDataObj,
    onBookEditClick: (Book) -> Unit,
    onAdminClick: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val booksListState = remember {
        mutableStateOf(emptyList<Book>())
    }

    val isAdminState = remember {
        mutableStateOf(false)
    }

    val dbState = remember {
        Firebase.firestore
    }

    val selectedBottomCategoryState = remember {
        mutableStateOf(BottomBarItem.Home.title)
    }

    LaunchedEffect(Unit) {
        getAllFavouritesIds(dbState, navData.uid) { favs ->
            getAllBooks(dbState, favs) { books ->
                booksListState.value = books
            }
        }
    }

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxWidth(),
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                DrawerHeader(navData.email)
                DrawerBody(
                    onAdmin = { isAdmin ->
                        isAdminState.value = isAdmin
                    },
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onAdminClick()
                    },
                    onFavouriteClick = {
                        selectedBottomCategoryState.value = BottomBarItem.Favourite.title

                        getAllFavouritesIds(dbState, navData.uid) { favs ->
                            getAllFavouriteBooks(dbState, favs) { books ->
                                booksListState.value = books
                            }
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    },
                    onCategoryClick = { category ->
                        selectedBottomCategoryState.value = BottomBarItem.Home.title

                        getAllFavouritesIds(dbState, navData.uid) { favs ->
                            if (category == "Разное") {
                                getAllBooks(dbState, favs) { books ->
                                    booksListState.value = books
                                }
                            } else {
                                getAllBooksFromCategory(dbState, favs, category) { books ->
                                    booksListState.value = books
                                }
                            }
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBar(
                    selectedBottomCategory = selectedBottomCategoryState.value,
                    onFavouritesClick = {
                        selectedBottomCategoryState.value = BottomBarItem.Favourite.title

                        getAllFavouritesIds(dbState, navData.uid) { favs ->
                            getAllFavouriteBooks(dbState, favs) { books ->
                                booksListState.value = books
                            }
                        }
                    },
                    onHomeClick = {
                        selectedBottomCategoryState.value = BottomBarItem.Home.title

                        getAllFavouritesIds(dbState, navData.uid) { favs ->
                            getAllBooks(dbState, favs) { books ->
                                booksListState.value = books
                            }
                        }
                    }
                )
            }
        ) { paddingValue -> //отвечает за отступы контента в зависимости от расположения элементов на экране у списка
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue),
                columns = GridCells.Fixed(2),
            ) {
                items(booksListState.value) { book ->
                    BookListItemUi(
                        showEditButton = isAdminState.value,
                        book = book,
                        onEditClick = {
                            onBookEditClick(it)
                        },
                        onFavouriteClick = {
                            booksListState.value = booksListState.value.map {
                                if (it.key == book.key) {
                                    onFavourites(
                                        db = dbState,
                                        uid = navData.uid,
                                        favourite = Favourite(it.key),
                                        isFavourite = !it.isFavourite
                                    )
                                    it.copy(isFavourite = !it.isFavourite)
                                } else {
                                    it
                                }
                            }
                            if (selectedBottomCategoryState.value == BottomBarItem.Favourite.title) {
                                booksListState.value =
                                    booksListState.value.filter { it.isFavourite }
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun getAllBooksFromCategory(
    db: FirebaseFirestore,
    idsList: List<String>,
    category: String,
    onBooks: (List<Book>) -> Unit,
) {
    db.collection("books")
        .whereEqualTo("category", category)
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.key)) {
                    it.copy(isFavourite = true)
                } else {
                    it
                }
            }
            onBooks(booksList)
        }
}

private fun getAllBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Book>) -> Unit,
) {
    db.collection("books")
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.key)) {
                    it.copy(isFavourite = true)
                } else {
                    it
                }
            }
            onBooks(booksList)
        }
}

private fun getAllFavouriteBooks(
    db: FirebaseFirestore,
    idsList: List<String>,
    onBooks: (List<Book>) -> Unit,
) {
    db.collection("books")
        .whereIn(FieldPath.documentId(), idsList)
        .get()
        .addOnSuccessListener { task ->
            val booksList = task.toObjects(Book::class.java).map {
                if (idsList.contains(it.key)) {
                    it.copy(isFavourite = true)
                } else {
                    it
                }
            }
            onBooks(booksList)
        }
}

private fun getAllFavouritesIds(
    db: FirebaseFirestore,
    uid: String,
    onFavourites: (List<String>) -> Unit,
) {
    db.collection("users")
        .document(uid)
        .collection("favourites")
        .get()
        .addOnSuccessListener { task ->
            val idsList = task.toObjects(Favourite::class.java)
            val keysList = mutableListOf<String>()

            idsList.forEach {
                keysList.add(it.key)
            }
            onFavourites(keysList)
        }
}

private fun onFavourites(
    db: FirebaseFirestore,
    uid: String,
    favourite: Favourite,
    isFavourite: Boolean,
) {
    if (isFavourite) {
        db.collection("users")
            .document(uid)
            .collection("favourites")
            .document(favourite.key)
            .set(favourite)
    } else {
        db.collection("users")
            .document(uid)
            .collection("favourites")
            .document(favourite.key)
            .delete()
    }
}