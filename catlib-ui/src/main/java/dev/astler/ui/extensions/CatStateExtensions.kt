package dev.astler.ui.extensions

import androidx.compose.runtime.Composable
import dev.astler.catlib.model.CatState

@Composable
fun CatState<*>.IsLoading(actionIfTrue: @Composable (() -> Unit)? = null) {
    if (this is CatState.Loading) {
        actionIfTrue?.invoke()
    }
}

@Composable
fun <T> CatState<T>.IsSuccessful(actionIfTrue: @Composable ((T) -> Unit)? = null) {
    if (this is CatState.Success) {
        actionIfTrue?.invoke(this.successData)
    }
}

@Composable
fun CatState<*>.IsFailed(actionIfTrue: @Composable ((String?) -> Unit)? = null) {
    if (this is CatState.Error) {
        actionIfTrue?.invoke(this.message)
    }
}