package dev.astler.catlib.helpers

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber

private const val baseTag = "ForAstler:"
private const val adsTag = "ForAstler:Ads:"
private const val signInTag = "ForAstler:SignIn:"
private const val errorTag = "ForAstler:Error:"

fun infoLog(pText: String, postCategory: String = "", category: String = baseTag) {
    Timber.tag(category + postCategory).i(pText)
}

fun adsLog(text: String, postCategory: String = "", category: String = adsTag) {
    infoLog(text, postCategory, category)
}

fun signInLog(text: String, postCategory: String = "", category: String = signInTag) {
    infoLog(text, postCategory, category)
}

fun errorLog(pText: String, postCategory: String = "", category: String = baseTag) {
    Firebase.crashlytics.log(pText)
    Timber.tag(category + postCategory).e(pText)
}

fun errorLog(
    exception: Exception?,
    text: String = "",
    postCategory: String = "",
    pCategory: String = errorTag
) {
    if (exception != null)
        Firebase.crashlytics.recordException(exception)

    errorLog(text + exception?.message, postCategory, pCategory)
}
