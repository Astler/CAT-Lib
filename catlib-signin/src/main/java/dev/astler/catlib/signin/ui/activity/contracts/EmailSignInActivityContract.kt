package dev.astler.catlib.signin.ui.activity.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.firebase.auth.FirebaseUser
import dev.astler.catlib.signin.ui.activity.SignInActivity
import dev.astler.catlib.signin.ui.activity.cSignInModeExtra

class EmailSignInActivityContract : ActivityResultContract<Int, FirebaseUser?>() {

    override fun createIntent(context: Context, input: Int): Intent {
        val intent = Intent(context, SignInActivity::class.java)
        intent.putExtra(cSignInModeExtra, input)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): FirebaseUser? {
        if (resultCode != Activity.RESULT_OK || intent == null) {
            return null
        }
        return intent.getParcelableExtra("FirebaseUser", FirebaseUser::class.java)
    }
}
