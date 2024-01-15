package dev.astler.catlib.signin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.astler.catlib.helpers.adsLog
import dev.astler.catlib.helpers.infoLog
import dev.astler.catlib.helpers.signInLog
import dev.astler.catlib.preferences.PreferencesTool
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val _preferences: PreferencesTool) : ViewModel() {
    private val _photoUrl: MutableLiveData<String?> = MutableLiveData(null)
    val photoUrl: LiveData<String?> = _photoUrl

    private val _signedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val signedIn: LiveData<Boolean> = _signedIn.asLiveData()

    private val _user: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    val user: LiveData<FirebaseUser?> = _user.asLiveData()

    fun setupUserData(user: FirebaseUser?) {
        signInLog("setupUserData started: $user")

        if (_user.value == user) return

        signInLog("setupUserData: $user")

        _user.value = user
        _signedIn.value = user != null
        _photoUrl.value = user?.photoUrl?.toString() ?: ""

        signInLog("photo: ${_photoUrl.value}")
    }
}