package com.dscreate_app.bookstoreapp.main_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                DrawerHeader()
                DrawerBody()
            }

        }
    ) {
        
    }
}