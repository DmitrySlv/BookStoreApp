package com.dscreate_app.bookstoreapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dscreate_app.bookstoreapp.data.Book
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val fireStore = Firebase.firestore
            val storage = Firebase.storage.reference.child(CHILD_NAME_IMAGE)

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri == null) return@rememberLauncherForActivityResult

                val task = storage
                    .child("battletoads.jpg")
                    .putBytes(bitmapToByteArray(this, uri))

                task.addOnSuccessListener { uploadTask ->
                    uploadTask.metadata?.reference?.downloadUrl?.addOnCompleteListener { taskUri ->
                        saveBook(fireStore, taskUri.result.toString())
                    }
                }
            }

            MainScreen {
                launcher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
    }

    @Composable
    fun MainScreen(onClick: () -> Unit) {
        val fireStore = Firebase.firestore

        val list = remember {
            mutableStateOf(emptyList<Book>())
        }

        val listener = fireStore.collection(COLLECTION_NAME)
            .addSnapshotListener { snapshot, error ->
                list.value = snapshot?.toObjects(Book::class.java) ?: emptyList()
            }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                items(list.value) { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .height(100.dp)
                                    .width(100.dp)
                                    .padding(16.dp),
                                model = book.imageUrl,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(),
                                text = book.name
                            )
                        }

                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = {
                    onClick()
                }
            ) {
                Text(
                    text = "Add book"
                )
            }
        }
    }

    private fun bitmapToByteArray(context: Context, uri: Uri): ByteArray {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_IMAGE, baos)
        return baos.toByteArray()
    }

    private fun saveBook(fireStore: FirebaseFirestore, url: String) {
        fireStore.collection(COLLECTION_NAME)
            .document()
            .set(
                Book(
                    "My book",
                    "something",
                    "100",
                    "fiction",
                    url
                )
            )
    }

    companion object {
        private const val COLLECTION_NAME = "books"
        private const val QUALITY_IMAGE = 50
        private const val CHILD_NAME_IMAGE = "images"
    }
}