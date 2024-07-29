package dev.astler.catlib.signin.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.ui.activities.BindingCatActivity
import dev.astler.ui.utils.setInsetsViaOrientation
import dev.astler.ui.utils.views.showWithCondition
import dev.astler.catlib.extensions.toast
import dev.astler.catlib.helpers.hasGoogleServices
import dev.astler.catlib.signin.R
import dev.astler.catlib.signin.SignInTool
import dev.astler.catlib.signin.data.CatSignInMode
import dev.astler.catlib.signin.data.CatSignInMode.Companion.fromString
import dev.astler.catlib.signin.databinding.SignInLayoutBinding
import dev.astler.catlib.signin.interfaces.ISignInListener
import dev.astler.catlib.signin.utils.startRegisterSignIn
import javax.inject.Inject

const val cSignInModeExtra = "signInMode"

@AndroidEntryPoint
open class SignInActivity: BindingCatActivity<SignInLayoutBinding>(SignInLayoutBinding::inflate), ISignInListener {

    @Inject
    lateinit var signInTool: SignInTool

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            val signInMode = intent.getStringExtra(cSignInModeExtra)?.fromString()
            val registerMode = signInMode == CatSignInMode.REGISTER
            val hasGoogleServices = hasGoogleServices()

            val canBeClosed =
                signInMode == CatSignInMode.OPTIONAL || signInMode == CatSignInMode.REGISTER

            secondPassword.showWithCondition(registerMode)
            orContinueWith.showWithCondition(!registerMode)
            registerHint.showWithCondition(!registerMode)
            googleSignIn.showWithCondition(!registerMode)

            notNow.showWithCondition(canBeClosed)

            notNow.setOnClickListener {
                this@SignInActivity.finish()
            }

            if (hasGoogleServices) {
                googleSignIn.setOnClickListener {
                    signInTool.tryToSignIn()
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
                        toast(R.string.not_all_fields)
                    } else {
                        if (nPasswordText != nPasswordAgainText) {
                            toast(R.string.passwords_dont_match)
                        } else handleAuthentication(nEmailText, nPasswordText, true)
                    }
                } else {
                    val nEmailText = email.text.toString()
                    val nPasswordText = password.text.toString()

                    if (nEmailText.isEmpty() || nPasswordText.isEmpty()) {
                        toast(R.string.not_all_fields)
                    } else {
                        handleAuthentication(nEmailText, nPasswordText, false)
                    }
                }
            }
        }

        setInsetsViaOrientation(binding.root)
    }

    override fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            this.finish()
        }
    }

    private fun handleAuthentication(email: String, password: String, isRegister: Boolean) {
        val authMethod = if (isRegister) {
            signInTool::createUserWithEmailAndPassword
        } else {
            signInTool::authWithEmailAndPassword
        }

        authMethod(email, password) { firebaseUser, message ->
            if (firebaseUser != null) {
                val resultIntent = Intent()

                resultIntent.putExtra("FirebaseUser", firebaseUser)
                setResult(Activity.RESULT_OK, resultIntent)

                finish()
            } else {
                toast(message ?: getString(gg.pressf.resources.R.string.something_went_wrong))
            }
        }
    }
}
