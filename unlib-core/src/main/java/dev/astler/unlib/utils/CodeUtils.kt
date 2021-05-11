package dev.astler.unlib.utils

fun simpleTryCatch(pAction: () -> Unit) {
    try {
        pAction()
    } catch (pException: Exception) {
        pException.printStackTrace()
        infoLog("Exception! ${pException.message}")
    }
}