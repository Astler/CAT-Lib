package dev.astler.unlib.utils

import android.util.Log

fun infoLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Log.i(pCategory + pPostCategory, pText)
}

fun adsLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAds") {
    Log.i(pCategory + pPostCategory, pText)
}

fun errorLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Log.e(pCategory + pPostCategory, pText)
}
