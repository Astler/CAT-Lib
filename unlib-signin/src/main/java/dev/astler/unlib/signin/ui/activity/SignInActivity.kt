package dev.astler.unlib.signin.ui.activity

import android.os.Bundle
import com.google.firebase.auth.FirebaseUser
import dev.astler.cat_ui.activities.CatActivity
import dev.astler.cat_ui.utils.setInsetsViaOrientation
import dev.astler.cat_ui.utils.views.goneView
import dev.astler.cat_ui.utils.views.showView
import dev.astler.unlib.signin.R
import dev.astler.unlib.signin.databinding.SignInLayoutBinding
import dev.astler.unlib.signin.interfaces.SignInActivityListener
import dev.astler.unlib.signin.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.utils.MobileServicesSource
import dev.astler.unlib.utils.getMobileServiceSource
import dev.astler.unlib.utils.infoLog
import dev.astler.unlib.utils.toast

const val cSignInModeExtra = "signInMode"

const val cMandatorySignIn = "mandatory"
const val cOptionalSignIn = "optional"
const val cOptionalJumpSignIn = "optional_jump"
const val cRegisterSignIn = "register"

open class SignInActivity : CatActivity<SignInLayoutBinding>(), SignInActivityListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(mViewBinding.root)

        val pMode = intent.getStringExtra(cSignInModeExtra).toString()

        signInInitializer()

        with(mViewBinding) {

            doNotAskAgain.goneView()

            register.setOnClickListener {
                this@SignInActivity.startRegisterSignIn()
                this@SignInActivity.finish()
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

            when (pMode) {
                cOptionalSignIn -> {
                    close.showView()
                    secondPassword.goneView()

                    close.setOnClickListener {
                        this@SignInActivity.finish()
                    }
                }
                cMandatorySignIn -> {
                    close.goneView()
                    secondPassword.goneView()
                }
                cRegisterSignIn -> {
                    close.showView()

                    close.setOnClickListener {
                        this@SignInActivity.finish()
                    }

                    signInButton.goneView()
                    googleSignIn.goneView()
                    or.goneView()

                    register.text = getString(R.string.create_account)

                    register.setOnClickListener {
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
                }
            }

            if (getMobileServiceSource() == MobileServicesSource.GOOGLE && pMode != cRegisterSignIn) {
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
