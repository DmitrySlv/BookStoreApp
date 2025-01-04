package com.dscreate_app.bookstoreapp.ui.login_screen

import android.content.Context
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dscreate_app.bookstoreapp.R
import com.dscreate_app.bookstoreapp.ui.LoginButton
import com.dscreate_app.bookstoreapp.ui.RoundedCornerTextField
import com.dscreate_app.bookstoreapp.ui.login_screen.models.MainScreenDataObj
import com.dscreate_app.bookstoreapp.ui.theme.BoxFilterColor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun LoginScreen(
    onNavigateToMainScreen: (MainScreenDataObj) -> Unit,
) {
    val context = LocalContext.current

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
            text = stringResource(R.string.book_store),
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif
        )
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = emailState.value,
            label = stringResource(R.string.email_title)
        ) {
            emailState.value = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        RoundedCornerTextField(
            text = passwordState.value,
            label = stringResource(R.string.password_title)
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

        LoginButton(text = stringResource(R.string.sign_in)) {
            signIn(
                context = context,
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
        LoginButton(text = stringResource(R.string.sign_up)) {
            signUp(
                context = context,
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
    context: Context,
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignUpSuccess: (MainScreenDataObj) -> Unit,
    onSignUpFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignUpFailure(context.getString(R.string.sign_up_failure_desc))
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
            onSignUpFailure(it.message ?: context.getString(R.string.sign_up_failure))
        }
}

fun signIn(
    context: Context,
    auth: FirebaseAuth,
    email: String,
    password: String,
    onSignInSuccess: (MainScreenDataObj) -> Unit,
    onSignInFailure: (String) -> Unit,
) {
    if (email.isBlank() || password.isBlank()) {
        onSignInFailure(context.getString(R.string.sign_in_failure_desc))
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
                onSignInFailure(context.getString(R.string.sign_in_failure))
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

