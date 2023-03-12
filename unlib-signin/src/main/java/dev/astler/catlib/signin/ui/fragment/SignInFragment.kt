package dev.astler.catlib.signin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.astler.cat_ui.fragments.CatFragment
import dev.astler.catlib.signin.utils.authWithEmailAndPassword
import dev.astler.catlib.signin.utils.signInWithGoogle
import dev.astler.catlib.signin.databinding.SignInLayoutBinding

open class SignInFragment : CatFragment<SignInLayoutBinding>() {

    private val mFragmentBinding by viewBinding<SignInLayoutBinding>()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> SignInLayoutBinding
        get() = SignInLayoutBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nActivity = requireActivity()

        with(mFragmentBinding) {
            googleSignIn.setOnClickListener {
                if (nActivity is AppCompatActivity) {
                    nActivity.signInWithGoogle()
                }
            }

            signInButton.setOnClickListener {
                val nEmailText = email.text.toString()
                val nPasswordText = password.text.toString()

                if (nActivity is AppCompatActivity) {
                    nActivity.authWithEmailAndPassword(nEmailText, nPasswordText)
                }
            }
        }
    }
}
