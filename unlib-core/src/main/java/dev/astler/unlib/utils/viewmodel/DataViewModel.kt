package dev.astler.unlib.utils.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

open class DataViewModel<T>(pApp: Application) : AndroidViewModel(pApp) {

    protected val mData = MutableLiveData<T>(null)

    fun getData(loadData: () -> T) = mData.loadDataWithCoroutine(viewModelScope) {
        loadData()
    }
}
