package dev.astler.catlib.helpers

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

fun infoLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Timber.tag("$pCategory $pPostCategory").i(pText)
}

fun adsLog(text: String, postCategory: String = "", category: String = "ForAds") {
    Timber.tag(category + postCategory).i(text)
}

fun errorLog(pText: String, pPostCategory: String = "", pCategory: String = "ForAstler") {
    Firebase.crashlytics.log(pText)
    Timber.tag(pCategory + pPostCategory).e(pText)
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
