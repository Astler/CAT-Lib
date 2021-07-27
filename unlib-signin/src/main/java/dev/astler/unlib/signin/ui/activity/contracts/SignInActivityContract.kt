package dev.astler.unlib.signin.ui.activity.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dev.astler.unlib.signin.utils.getGoogleSignInClient
import dev.astler.unlib.utils.infoLog

class SignInActivityContract : ActivityResultContract<String, Task<GoogleSignInAccount>?>() {

    override fun createIntent(context: Context, input: String): Intent {
        infoLog("Sign In Intent Launched: $input")

        return context.getGoogleSignInClient().signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        infoLog("result code = $resultCode")

        return when {
            resultCode != Activity.RESULT_OK -> null
            else -> GoogleSignIn.getSignedInAccountFromIntent(intent)
        }
    }
}
