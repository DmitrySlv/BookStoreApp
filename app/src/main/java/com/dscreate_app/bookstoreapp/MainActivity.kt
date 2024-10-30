package com.dscreate_app.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dscreate_app.bookstoreapp.add_book_scrren.AddBookScreen
import com.dscreate_app.bookstoreapp.add_book_scrren.data.AddScreenObj
import com.dscreate_app.bookstoreapp.login.LoginScreen
import com.dscreate_app.bookstoreapp.login.data.LoginScreenObject
import com.dscreate_app.bookstoreapp.login.data.MainScreenDataObj
import com.dscreate_app.bookstoreapp.main_screen.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = LoginScreenObject
            ) {
                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)
                    }
                }
                composable<MainScreenDataObj> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObj>()
                    MainScreen(navData) {
                        navController.navigate(AddScreenObj)
                    }
                }
                composable<AddScreenObj> { navEntry ->
                    AddBookScreen {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}