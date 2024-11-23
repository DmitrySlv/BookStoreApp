package com.dscreate_app.bookstoreapp.add_book_scrren

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dscreate_app.bookstoreapp.LoginButton
import com.dscreate_app.bookstoreapp.R
import com.dscreate_app.bookstoreapp.RoundedCornerTextField
import com.dscreate_app.bookstoreapp.add_book_scrren.data.AddScreenObj
import com.dscreate_app.bookstoreapp.data.Book
import com.dscreate_app.bookstoreapp.ui.theme.BoxFilterColor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

@Preview(showBackground = true)
@Composable
fun AddBookScreen(
    navData: AddScreenObj = AddScreenObj(),
    onSaved: () -> Unit = {}
) {

    var selectedCategory = navData.category

    val titleState = remember {
        mutableStateOf(navData.title)
    }

    val descriptionState = remember {
        mutableStateOf(navData.description)
    }

    val priceState = remember {
        mutableStateOf(navData.price)
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val fireStore = remember {
        Firebase.firestore
    }

    val storage = remember {
        Firebase.storage
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri.value = uri
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = rememberAsyncImagePainter(model = selectedImageUri.value),
        contentDescription = null,
        alpha = 0.8f,
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BoxFilterColor)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 40.dp, end = 40.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.size(90.dp),
            painter = painterResource(id = R.drawable.books),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Добавить книгу",
            color = Color.White,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerDropDownMenu { selectedItem ->
            selectedCategory = selectedItem
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = titleState.value,
            label = "Заголовок"
        ) {
            titleState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            maxLines = 5,
            singleLine = false,
            text = descriptionState.value,
            label = "Описание"
        ) {
            descriptionState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = priceState.value,
            label = "Цена"
        ) {
            priceState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        LoginButton(text = "Выбрать изображение") {
            imageLauncher.launch("image/*")
        }
        Spacer(modifier = Modifier.height(5.dp))
        LoginButton(text = "Сохранить") {
            saveBookImage(
                uri = selectedImageUri.value!!,
                storage = storage,
                fireStore = fireStore,
                book =  Book(
                    title = titleState.value,
                    description = descriptionState.value,
                    price = priceState.value,
                    category = selectedCategory
                ),
                onSaved = {
                    onSaved()
                },
                onError = {}
            )
        }
    }
}

private fun saveBookImage(
    uri: Uri,
    storage: FirebaseStorage,
    fireStore: FirebaseFirestore,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit,
) {
    val timeStamp = System.currentTimeMillis()
    val storageRef = storage.reference
        .child("book_images")
        .child("image_$timeStamp.jpg")
    val uploadTask = storageRef.putFile(uri)
    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { url ->
            saveBookToFireStore(
                fireStore = fireStore,
                url = url.toString(),
                book = book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError()
                }
            )
        }
    }
}

private fun saveBookToFireStore(
    fireStore: FirebaseFirestore,
    url: String,
    book: Book,
    onSaved: () -> Unit,
    onError: () -> Unit,
) {
    val db = fireStore.collection("books")
    val key = db.document().id
    db.document(key)
        .set(
            book.copy(
                key = key,
                imageUrl = url
            )
        )
        .addOnSuccessListener {
            onSaved()
        }
        .addOnFailureListener {
            onError()
        }
}