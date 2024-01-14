package dev.astler.catlib.signin.ui.fragment

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import dev.astler.cat_ui.fragments.CatFragment
import dev.astler.catlib.signin.SignInTool
import dev.astler.catlib.signin.databinding.SignInLayoutBinding
import javax.inject.Inject

@AndroidEntryPoint
open class SignInFragment : CatFragment<SignInLayoutBinding>(SignInLayoutBinding::inflate) {

    @Inject
    lateinit var signInTool: SignInTool

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            googleSignIn.setOnClickListener {
                signInTool.tryToSignInWithGoogle()
            }

            signInButton.setOnClickListener {
                val emailValue = email.text.toString()
                val passwordValue = password.text.toString()

                signInTool.authWithEmailAndPassword(emailValue, passwordValue) { user, error ->
                    signInTool.processEmailSignIn(user)
                }
            }
        }
    }
}
