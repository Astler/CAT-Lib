package dev.astler.catlib.utils

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.lang.Exception

fun infoLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Log.i("$pCategory $pPostCategory", pText)
}

fun adsLog(text: String, postCategory: String = "", category: String = "ForAds") {
    Log.i(category + postCategory, text)
}

fun errorLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Firebase.crashlytics.log(pText)
    Log.e(pCategory + pPostCategory, pText)
}

fun errorLog(
    exception: Exception?,
    text: String = "",
    pPostCategory: String = "",
    pCategory: String = "ForAstler"
) {
    if (exception != null)
        Firebase.crashlytics.recordException(exception)

    errorLog(text, pPostCategory, pCategory)
}
