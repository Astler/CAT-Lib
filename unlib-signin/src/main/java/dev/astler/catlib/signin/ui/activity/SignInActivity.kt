package dev.astler.catlib.signin.ui.activity

import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.utils.setInsetsViaOrientation
import dev.astler.cat_ui.utils.views.showViewWithCondition
import dev.astler.catlib.signin.R
import dev.astler.catlib.signin.databinding.SignInLayoutBinding
import dev.astler.catlib.signin.interfaces.SignInActivityListener
import dev.astler.catlib.signin.ui.activity.CatSignInMode.Companion.fromString
import dev.astler.catlib.signin.utils.authWithEmailAndPassword
import dev.astler.catlib.signin.utils.createUserWithEmailAndPassword
import dev.astler.catlib.signin.utils.signInInitializer
import dev.astler.catlib.signin.utils.signInOnResume
import dev.astler.catlib.signin.utils.signInWithGoogle
import dev.astler.catlib.signin.utils.startRegisterSignIn
import dev.astler.catlib.utils.MobileServicesSource
import dev.astler.catlib.utils.getMobileServiceSource
import dev.astler.catlib.utils.infoLog
import dev.astler.catlib.utils.toast

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

@AndroidEntryPoint
open class SignInActivity : CatActivity(), SignInActivityListener {

    lateinit var binding: SignInLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SignInLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        signInInitializer()

        with(binding) {
            val signInMode = intent.getStringExtra(cSignInModeExtra)?.fromString()
            val registerMode = signInMode == CatSignInMode.REGISTER
            val hasGoogleServices = getMobileServiceSource() == MobileServicesSource.GOOGLE

            val canBeClosed =
                signInMode == CatSignInMode.OPTIONAL || signInMode == CatSignInMode.REGISTER

            secondPassword.showViewWithCondition(registerMode)
            orContinueWith.showViewWithCondition(!registerMode)
            registerHint.showViewWithCondition(!registerMode)
            googleSignIn.showViewWithCondition(!registerMode)

            notNow.showViewWithCondition(canBeClosed)

            notNow.setOnClickListener {
                this@SignInActivity.finish()
            }

            if (hasGoogleServices) {
                googleSignIn.setOnClickListener {
                    signInWithGoogle()
                }
            }

            createNew.setOnClickListener {
                this@SignInActivity.startRegisterSignIn()
                this@SignInActivity.finish()
            }

            signInButton.text =
                getString(if (registerMode) R.string.create_account else R.string.sign_in_email)

            signInButton.setOnClickListener {
                if (signInMode == CatSignInMode.REGISTER) {
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
                } else {
                    val nEmailText = email.text.toString()
                    val nPasswordText = password.text.toString()

                    if (nEmailText.isEmpty() || nPasswordText.isEmpty()) {
                        it.toast(R.string.not_all_fields)
                    } else {
                        authWithEmailAndPassword(nEmailText, nPasswordText)
                    }
                }
            }
        }

        setInsetsViaOrientation(binding.root)
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
}