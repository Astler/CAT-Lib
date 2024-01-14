package dev.astler.catlib.signin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow

class SignInViewModel: ViewModel() {
    private val _photoUrl: MutableLiveData<String?> = MutableLiveData(null)
    val photoUrl: LiveData<String?> = _photoUrl

    private val _signedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val signedIn: LiveData<Boolean> = _signedIn.asLiveData()

    private val _user: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    val user: LiveData<FirebaseUser?> = _user.asLiveData()

    fun setupUserData(user: FirebaseUser?) {
        if (_user.value == user) return

        _user.value = user
        _signedIn.value = user != null

        if (_photoUrl.value == null) {
            _photoUrl.value = user?.photoUrl?.toString() ?: ""
        }
    }
}