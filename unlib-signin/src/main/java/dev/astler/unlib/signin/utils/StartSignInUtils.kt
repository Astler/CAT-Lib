package dev.astler.unlib.signin.utils

import android.content.Context
import android.content.Intent
import dev.astler.unlib.signin.ui.activity.* // ktlint-disable no-wildcard-imports

fun Context.startMandatorySignIn() {
    startSignInActivity(CatSignInMode.MANDATORY.toString())
}

fun Context.startOptionalSignIn() {
    startSignInActivity(CatSignInMode.OPTIONAL.toString())
}

fun Context.startRegisterSignIn() {
    startSignInActivity(CatSignInMode.REGISTER.toString())
}

private fun Context.startSignInActivity(signInMode: String) {
    val intent = Intent(this, SignInActivity::class.java)
    intent.putExtra(cSignInModeExtra, signInMode)
    startActivity(intent)
}
