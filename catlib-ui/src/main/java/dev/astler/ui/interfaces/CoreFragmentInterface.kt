package dev.astler.ui.interfaces

interface CoreFragmentInterface {
    fun onFragmentBackPressed(endAction: () -> Unit = {}) {}
}
