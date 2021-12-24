package dev.astler.unlib.signin.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.astler.cat_ui.fragments.CatFragment
import dev.astler.unlib.signin.R
import dev.astler.unlib.signin.databinding.SignInLayoutBinding
import dev.astler.unlib.signin.utils.authWithEmailAndPassword
import dev.astler.unlib.signin.utils.signInWithGoogle

class SignInFragment(private val mId: Int = R.layout.sign_in_layout) : CatFragment(mId) {

    private val mFragmentBinding by viewBinding<SignInLayoutBinding>()

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

    override fun onFragmentBackPressed(endAction: () -> Unit) {

    }
}
