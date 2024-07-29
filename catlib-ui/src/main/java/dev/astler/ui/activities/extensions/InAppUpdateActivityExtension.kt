package dev.astler.ui.activities.extensions

import android.app.Activity
import android.content.IntentSender
import android.view.View
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dev.astler.ui.activities.CatActivity
import dev.astler.catlib.extensions.toast
import dev.astler.catlib.helpers.errorLog
import dev.astler.catlib.helpers.infoLog
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

class InAppUpdateActivityExtension(private val _catActivity: CatActivity, snapParentView: View) {

    private val _updateRequestCode = 101
    private val _appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(_catActivity) }

    private val updateLauncher = _catActivity.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.data == null) return@registerForActivityResult
        if (result.resultCode == _updateRequestCode) {
            _catActivity.toast("Downloading stated")
            if (result.resultCode != Activity.RESULT_OK) {
                _catActivity.toast("Downloading failed")
            }
        }
    }

    private val _listener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADING) {
            val bytesDownloaded = state.bytesDownloaded()
            val totalBytesToDownload = state.totalBytesToDownload()
            infoLog("InAppUpdateActivityExtension: Downloading: $bytesDownloaded / $totalBytesToDownload")
        }
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Snackbar.make(
                snapParentView,
                "New app is ready",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Restart") {
                _appUpdateManager.completeUpdate()
            }.show()
        }
    }

    private val _updateResultStarter =
        IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
            val request = IntentSenderRequest.Builder(intent)
                .setFillInIntent(fillInIntent)
                .setFlags(flagsValues, flagsMask)
                .build()
            updateLauncher.launch(request)
        }

    init {
        checkUpdate()
        _catActivity.lifecycleScope.launch {
            _catActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                infoLog("InAppUpdateActivityExtension: registered listener")
                _appUpdateManager.registerListener(_listener)
                try {
                    awaitCancellation()
                } finally {
                    infoLog("InAppUpdateActivityExtension: unregistered listener")
                    _appUpdateManager.unregisterListener(_listener)
                }
            }
        }
    }

    private fun checkUpdate() {
        infoLog("InAppUpdateActivityExtension: checkUpdate")
        _appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                try {
                    _appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        _updateResultStarter,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                        _updateRequestCode
                    )
                } catch (exception: IntentSender.SendIntentException) {
                    _catActivity.toast("InAppUpdateActivityExtension: Something went wrong!")
                }
            } else {
                infoLog("InAppUpdateActivityExtension: No Update available")
            }
        }.addOnFailureListener { exception ->
            errorLog(exception, "InAppUpdateActivityExtension: ")
        }
    }
}