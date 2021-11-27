package dev.astler.unlib.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> MutableLiveData<T>.loadDataWithCoroutine(
    pScope: CoroutineScope,
    pReloadEveryRequest: Boolean = false,
    pLoadData: () -> T
): LiveData<T> {
    if (this.value == null || pReloadEveryRequest) {
        pScope.launch(Dispatchers.IO) {
            val nData = pLoadData()

            withContext(Dispatchers.Main) {
                value = nData
            }
        }
    }

    return this
}

class DataViewModel<T>(pApp: Application) : AndroidViewModel(pApp) {

    private val mData = MutableLiveData<T>(null)

    fun getData(loadData: () -> T) = mData.loadDataWithCoroutine(viewModelScope) {
        loadData()
    }
}
