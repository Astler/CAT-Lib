package dev.astler.unlib_compose.interfaces

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.astler.unlib_compose.data.IFlexibleItem

interface IComposeItem<T: IFlexibleItem, B> {
    val item: T
    @Composable
    fun Content(modifier: Modifier, onClick: ((B) -> Unit)?)
}