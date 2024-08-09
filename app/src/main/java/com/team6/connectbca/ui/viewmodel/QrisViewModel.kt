package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.QrVerifyUseCase
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class QrisViewModel(
    private val qrisVerifyUseCase: QrVerifyUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    fun verifyQr(payload: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val sessionData = getSessionDataUseCase()
                val token = sessionData.getValue("token") as String
                val res = qrisVerifyUseCase(token, payload)
                if (res.status == "Success") {
                    _success.value = true
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

    fun getError(): LiveData<Throwable> {
        return _error
    }

    fun getSuccess(): LiveData<Boolean> {
        return _success
    }

    fun resetSuccess() {
        _success.value = false
    }

    fun resetError() {
        _error.value = Throwable()
    }

    fun getLoading(): LiveData<Boolean> {
        return _loading
    }
}