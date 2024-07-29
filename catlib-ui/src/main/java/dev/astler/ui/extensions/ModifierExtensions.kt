package dev.astler.ui.extensions

import androidx.compose.ui.Modifier

fun Modifier.thenIf(condition: Boolean, other: Modifier): Modifier =
    if (condition) this.then(other) else this
