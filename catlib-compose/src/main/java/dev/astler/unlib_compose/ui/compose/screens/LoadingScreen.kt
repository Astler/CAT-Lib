package dev.astler.unlib_compose.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.astler.catlib.compose.R
import dev.astler.unlib_compose.ui.compose.components.EnchantedText

@Composable
fun LoadingScreen(textSize: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(modifier = Modifier.size(100.dp), model = R.drawable.search, contentDescription = null)
            EnchantedText(
                modifier = Modifier.padding(16.dp),
                text = stringResource(id = dev.astler.catlib.ui.R.string.loading),
                centered = true,
                isBold = true,
                textSize = textSize + 8
            )
        }
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}