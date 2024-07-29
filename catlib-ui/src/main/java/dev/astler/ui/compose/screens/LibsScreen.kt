package dev.astler.ui.compose.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer

@Composable
fun LibsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        LibrariesContainer(
            Modifier.fillMaxSize()
        )
    }
}