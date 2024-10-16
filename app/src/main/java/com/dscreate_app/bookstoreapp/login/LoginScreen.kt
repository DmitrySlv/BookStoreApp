package com.dscreate_app.bookstoreapp.login

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dscreate_app.bookstoreapp.LoginButton
import com.dscreate_app.bookstoreapp.R
import com.dscreate_app.bookstoreapp.RoundedCornerTextField
import com.dscreate_app.bookstoreapp.login.data.MainScreenDataObj
import com.dscreate_app.bookstoreapp.ui.theme.BoxFilterColor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    onNavigateToMainScreen: (MainScreenDataObj) -> Unit
) {

    val auth = remember {
        Firebase.auth
    }

    val emailState = remember {
        mutableStateOf("dscreateapp@gmail.com")
    }

    val passwordState = remember {
        mutableStateOf("123456789")
    }

    val errorState = remember {
        mutableStateOf("")
    }

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.bookstore),
        contentDescription = null,
        alpha = 0.8f,
        contentScale = ContentScale.Crop
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BoxFilterColor)
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
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.books),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Книжный магазин",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = emailState.value,
            label = "Email"
        ) {
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            label = "Password"
        ) {
            passwordState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .background(color = Color.White)
                .alpha(0.7f)
        ) {
            if (errorState.value.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 2.dp,
                            bottom = 2.dp
                        ),
                    text = errorState.value,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        LoginButton(text = "Войти") {
            signIn(
                auth = auth,
                email = emailState.value,
                password = passwordState.value,
                onSignInSuccess = { navData ->
                    onNavigateToMainScreen(navData)
                },
                onSignInFailure = { error ->
                    errorState.value = error
                }
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        LoginButton(text = "Регистрация") {
            signUp(
                auth = auth,
                email = emailState.value,
                password = passwordState.value,
                onSignUpSuccess = { navData ->
                    onNavigateToMainScreen(navData)
                },
                onSignUpFailure = { error ->
                   errorState.value = error
                }
            )
        }
    }
}

fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: (MainScreenDataObj) -> Unit,
    onSignUpFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure("Email и пароль не могут быть пустыми!")
        return
    }
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignUpSuccess(
                    MainScreenDataObj(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            }
        }
        .addOnFailureListener {
            onSignUpFailure(it.message ?: "Ошибка регистрации аккаунта!")
        }
}

fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess: (MainScreenDataObj) -> Unit,
    onSignInFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure("Email и пароль не могут быть пустыми!")
        return
    }
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInSuccess(
                    MainScreenDataObj(
                        task.result.user?.uid!!,
                        task.result.user?.email!!
                    )
                )
            } else {
               onSignInFailure("Ошибка входа в аккаунт!")
            }
        }
}

private fun signOut(auth: FirebaseAuth) {
    auth.signOut()
}

private fun deleteAccount(
    auth: FirebaseAuth,
    email: String,
    password: String,
) {
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {
        if (it.isSuccessful) {
            auth.currentUser?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MyLog", "Account deleted successful")
                } else {
                    Log.d("MyLog", "Account delete is failure!")
                }
            }
        } else {
            Log.d("MyLog", "Failure reauthenticate!")
        }
    }
}

