package dev.astler.unlib.signin.ui.activity.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import dev.astler.unlib.signin.R
import dev.astler.unlib.signin.utils.getGoogleSignInClient
import dev.astler.unlib.utils.infoLog

class SignInActivityContract : ActivityResultContract<String, Task<GoogleSignInAccount>?>() {

    override fun createIntent(context: Context, input: String): Intent {
        infoLog("Sign In Intent Launched: $input")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        infoLog("TOKEN code = ${context.getString(R.string.default_web_client_id)}")

        return GoogleSignIn.getClient(context, gso).signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Task<GoogleSignInAccount>? {
        infoLog("result code = $resultCode")
        infoLog("result data = ${intent?.data}")
        infoLog("result googleSignInStatus = ${intent?.extras?.get("googleSignInStatus")}")
        intent?.extras?.keySet()?.forEach {
            infoLog("result extras = $it")
        }

        return when {
            resultCode != Activity.RESULT_OK -> null
            else -> GoogleSignIn.getSignedInAccountFromIntent(intent)
        }
    }
}
