package dev.astler.catlib.utils.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> MutableLiveData<T>.loadDataWithCoroutine(
    pScope: CoroutineScope,
    pReloadEveryRequest: Boolean = false,
    pLoadData: suspend () -> T
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
