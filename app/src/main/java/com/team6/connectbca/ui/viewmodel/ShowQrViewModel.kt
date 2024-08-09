package com.team6.connectbca.ui.viewmodel

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.usecase.ShowQrUseCase
import kotlinx.coroutines.launch

class ShowQrViewModel(private val showQrUseCase: ShowQrUseCase) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val qrImage: MutableLiveData<String> = MutableLiveData()
    fun showQr(isDarkTheme: Boolean) {

        viewModelScope.launch {
            try {
                _loading.value = true
                val res = showQrUseCase(mode = if (isDarkTheme) "dark" else "", option = "url")
                Log.d("ShowQr", res.toString())
                if (res.status == "Success") {
                    _success.value = true
                    qrImage.value = res.data?.qrImage
                } else {
                    _error.value = Throwable(res.message)

                }
                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }
    }


}