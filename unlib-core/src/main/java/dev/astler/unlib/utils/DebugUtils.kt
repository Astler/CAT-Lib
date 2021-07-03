package dev.astler.unlib.utils

import android.util.Log

fun adLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAd") {
    Log.i(pCategory + pPostCategory, pText)
}

fun infoLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Log.i(pCategory + pPostCategory, pText)
}

fun errorLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Log.e(pCategory + pPostCategory, pText)
}
