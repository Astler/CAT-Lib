package dev.astler.catlib.helpers

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import dev.astler.catlib.constants.MimetypeImages
import dev.astler.catlib.constants.RegistryKey

class ImagePicker(
    pActivityResultRegistry: ActivityResultRegistry,
    pCallback: (pImageUri: Uri?) -> Unit
) {
    private val mGetContent: ActivityResultLauncher<String> =
        pActivityResultRegistry.register(RegistryKey, ActivityResultContracts.GetContent(), pCallback)

    fun pickImage() {
        mGetContent.launch(MimetypeImages)
    }
}