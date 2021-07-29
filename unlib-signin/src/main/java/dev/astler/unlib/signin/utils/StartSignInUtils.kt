package dev.astler.unlib.signin.utils

import android.content.Context
import android.content.Intent
import dev.astler.unlib.signin.ui.activity.* // ktlint-disable no-wildcard-imports

fun Context.startMandatorySignIn() {
    startSignInActivity(cMandatorySignIn)
}

fun Context.startOptionalSignIn() {
    startSignInActivity()
}

fun Context.startRegisterSignIn() {
    startSignInActivity(cRegisterSignIn)
}

private fun Context.startSignInActivity(pExtraValue: String = cOptionalSignIn) {
    val intent = Intent(this, SignInActivity::class.java)
    intent.putExtra(cSignInModeExtra, pExtraValue)
    startActivity(intent)
}
