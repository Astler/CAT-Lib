package dev.astler.unlib.signin.ui.activity

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseUser
import dev.astler.unlib.signin.R
import dev.astler.unlib.signin.databinding.SignInLayoutBinding
import dev.astler.unlib.signin.interfaces.SignInActivityListener
import dev.astler.unlib.signin.utils.* // ktlint-disable no-wildcard-imports
import dev.astler.unlib.ui.activity.BaseUnLiActivity
import dev.astler.unlib.utils.* // ktlint-disable no-wildcard-imports

const val cSignInModeExtra = "signInMode"

const val cMandatorySignIn = "mandatory"
const val cOptionalSignIn = "optional"
const val cOptionalJumpSignIn = "optional_jump"
const val cRegisterSignIn = "register"

open class SignInActivity : BaseUnLiActivity(), SignInActivityListener {

    private val mActivityMainBinding: SignInLayoutBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(mActivityMainBinding.root)

        val pMode = intent.getStringExtra(cSignInModeExtra).toString()

        signInInitializer()

        with(mActivityMainBinding) {

            doNotAskAgain.goneView()

            register.setOnClickListener {
                this@SignInActivity.startRegisterSignIn()
                this@SignInActivity.finish()
            }

            when (pMode) {
                cOptionalSignIn -> {
                    close.showView()

                    close.setOnClickListener {
                        this@SignInActivity.finish()
                    }
                }
                cMandatorySignIn -> {
                    close.goneView()
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

                        createUserWithEmailAndPassword(nEmailText, nPasswordText)
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
