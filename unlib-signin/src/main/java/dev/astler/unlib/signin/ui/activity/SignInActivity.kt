package dev.astler.unlib.signin.ui.activity

import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.utils.setInsetsViaOrientation
import dev.astler.cat_ui.utils.views.goneView
import dev.astler.cat_ui.utils.views.goneViews
import dev.astler.cat_ui.utils.views.showView
import dev.astler.unlib.signin.R
import dev.astler.unlib.signin.databinding.SignInLayoutBinding
import dev.astler.unlib.signin.interfaces.SignInActivityListener
import dev.astler.unlib.signin.ui.activity.CatSignInMode.Companion.fromString
import dev.astler.unlib.signin.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.utils.MobileServicesSource
import dev.astler.unlib.utils.getMobileServiceSource
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.toast

const val cSignInModeExtra = "signInMode"

enum class CatSignInMode {
    MANDATORY,
    OPTIONAL,
    OPTIONAL_JUMP,
    REGISTER;

    companion object {
        fun String.fromString() = valueOf(this)
    }
}

open class SignInActivity : CatActivity<SignInLayoutBinding>(), SignInActivityListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(mViewBinding.root)

        signInInitializer()

        with(mViewBinding) {

            doNotAskAgain.goneView()

            register.setOnClickListener {
                this@SignInActivity.startRegisterSignIn()
                this@SignInActivity.finish()
            }

            createAccount.setOnClickListener {
                val nEmailText = email.text.toString()
                val nPasswordText = password.text.toString()
                val nPasswordAgainText = passwordAgain.text.toString()

                if (nEmailText.isEmpty() || nPasswordText.isEmpty() || nPasswordAgainText.isEmpty()) {
                    it.toast(R.string.not_all_fields)
                } else {
                    if (nPasswordText != nPasswordAgainText) {
                        it.toast(R.string.passwords_dont_match)
                    } else createUserWithEmailAndPassword(nEmailText, nPasswordText)
                }
            }

            signInButton.setOnClickListener {
                val nEmailText = email.text.toString()
                val nPasswordText = password.text.toString()

                if (nEmailText.isEmpty() || nPasswordText.isEmpty()) {
                    it.toast(R.string.not_all_fields)
                } else {
                    authWithEmailAndPassword(nEmailText, nPasswordText)
                }
            }

            val signInMode = intent.getStringExtra(cSignInModeExtra)?.fromString()

            when (signInMode) {
                CatSignInMode.OPTIONAL -> {
                    close.showView()
                    secondPassword.goneView()

                    close.setOnClickListener {
                        this@SignInActivity.finish()
                    }
                }
                CatSignInMode.MANDATORY -> {
                    close.goneView()
                    secondPassword.goneView()
                }
                CatSignInMode.REGISTER -> {
                    close.showView()

                    close.setOnClickListener {
                        this@SignInActivity.finish()
                    }

                    goneViews(
                        register,
                        signInButton,
                        googleSignIn,
                        or
                    )
                }
                else -> {}
            }

            if (getMobileServiceSource() == MobileServicesSource.GOOGLE && signInMode != CatSignInMode.REGISTER) {
                googleSignIn.setOnClickListener {
                    signInWithGoogle()
                }
            } else {
                or.goneView()
                googleSignIn.goneView()
            }
        }

        setInsetsViaOrientation(mViewBinding.root)
    }

    override fun onResume() {
        super.onResume()
        signInOnResume()
    }

    override fun updateUI(pUser: FirebaseUser?) {
        if (pUser != null) {
            this.finish()
        } else {
            infoLog("No Active Users!")
        }
    }

    override fun getViewBinding() = SignInLayoutBinding.inflate(layoutInflater)
}
