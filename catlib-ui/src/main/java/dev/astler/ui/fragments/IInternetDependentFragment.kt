package dev.astler.ui.fragments

interface IInternetDependentFragment {
    fun onInternetAvailable()
    fun onInternetLost()
}