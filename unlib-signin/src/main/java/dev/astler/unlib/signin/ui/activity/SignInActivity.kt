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
import dev.astler.unlib.utils.goneView
import dev.astler.unlib.utils.infoLog

open class SignInActivity : BaseUnLiActivity(), SignInActivityListener {

    private val mActivityMainBinding: SignInLayoutBinding by viewBinding(CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(mActivityMainBinding.root)

        signInInitializer()

        with(mActivityMainBinding) {
            googleSignIn.setOnClickListener {
                signInWithGoogle()
            }

            signInButton.setOnClickListener {
                val nEmailText = email.text.toString()
                val nPasswordText = password.text.toString()

                authWithEmailAndPassword(nEmailText, nPasswordText)
            }

            register.setOnClickListener {
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
