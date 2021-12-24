package dev.astler.cat_ui.interfaces

interface CoreFragmentInterface {
    fun onFragmentBackPressed(endAction: () -> Unit = {})
}
