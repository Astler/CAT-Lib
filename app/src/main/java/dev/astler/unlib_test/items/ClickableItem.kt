package dev.astler.unlib_test.items

import android.view.View

data class ClickableItem(val text: String, val pAction: (pView: View) -> Unit)
