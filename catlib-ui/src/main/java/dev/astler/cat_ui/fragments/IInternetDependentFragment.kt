package dev.astler.cat_ui.fragments

interface IInternetDependentFragment {
    fun onInternetAvailable()
    fun onInternetLost()
}