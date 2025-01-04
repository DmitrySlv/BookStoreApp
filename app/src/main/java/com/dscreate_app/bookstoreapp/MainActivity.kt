package com.dscreate_app.bookstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.dscreate_app.bookstoreapp.ui.add_book_scrren.AddBookScreen
import com.dscreate_app.bookstoreapp.ui.add_book_scrren.models.AddScreenObj
import com.dscreate_app.bookstoreapp.ui.details_screen.models.DetailsNavObj
import com.dscreate_app.bookstoreapp.ui.details_screen.DetailsScreen
import com.dscreate_app.bookstoreapp.ui.login_screen.LoginScreen
import com.dscreate_app.bookstoreapp.ui.login_screen.models.LoginScreenObject
import com.dscreate_app.bookstoreapp.ui.login_screen.models.MainScreenDataObj
import com.dscreate_app.bookstoreapp.ui.main_screen.MainScreen

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
                    MainScreen(
                        navData,
                        onBookEditClick = { book ->
                            navController.navigate(
                                AddScreenObj(
                                key = book.key,
                                title = book.title,
                                description = book.description,
                                price = book.price,
                                category = book.category,
                                imageUrl = book.imageUrl
                            )
                            )
                        },
                        onAdminClick = {
                            navController.navigate(AddScreenObj())
                        },
                        onBookClick = { bookItem ->
                            navController.navigate(DetailsNavObj(
                                title = bookItem.title,
                                description = bookItem.description,
                                price = bookItem.price,
                                category = bookItem.category,
                                imageUrl = bookItem.imageUrl
                            ))
                        }
                    )
                }
                composable<AddScreenObj> { navEntry ->
                    val navData = navEntry.toRoute<AddScreenObj>()
                    AddBookScreen(navData)
                }
                composable<DetailsNavObj> { navEntry ->
                    val navData = navEntry.toRoute<DetailsNavObj>()
                    DetailsScreen(navData)
                }
            }
        }
    }
}