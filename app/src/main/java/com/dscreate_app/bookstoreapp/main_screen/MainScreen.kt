package com.dscreate_app.bookstoreapp.main_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.dscreate_app.bookstoreapp.login.data.MainScreenDataObj
import com.dscreate_app.bookstoreapp.main_screen.bottom_menu.BottomBar
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navData: MainScreenDataObj,
    onAdminClick: () -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxWidth(),
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                DrawerHeader(navData.email)
                DrawerBody {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    onAdminClick()
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomBar()
            }
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(10) {
                    BookListItemUi()
                }
            }
        }
    }
}