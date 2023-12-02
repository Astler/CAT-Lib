package dev.astler.catlib.signin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import dev.astler.cat_ui.fragments.CatFragment
import dev.astler.catlib.signin.utils.authWithEmailAndPassword
import dev.astler.catlib.signin.utils.signInWithGoogle
import dev.astler.catlib.signin.databinding.SignInLayoutBinding

open class SignInFragment : CatFragment<SignInLayoutBinding>(SignInLayoutBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()

        with(binding) {
            googleSignIn.setOnClickListener {
                if (activity is AppCompatActivity) {
                    activity.signInWithGoogle()
                }
            }

            signInButton.setOnClickListener {
                val emailValue = email.text.toString()
                val passwordValue = password.text.toString()

                if (activity is AppCompatActivity) {
                    activity.authWithEmailAndPassword(emailValue, passwordValue)
                }
            }
        }
    }
}
