package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _sessionData: MutableLiveData<Map<String, Any>> = MutableLiveData<Map<String, Any>>()

    fun userLogin(userId: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val res = authRepository.userLogin(userId, password)
                if (res) {
                    _success.value = true
                } else {
                    _error.value = UnsupportedOperationException("Terdapat kesalahan saat login")
                }
                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }
    }

    fun userLogout() {
        viewModelScope.launch {
            try {
                _loading.value = true
                authRepository.clearToken()
                authRepository.clearSessionTime()
                _loading.value = false
                _success.value = true
            } catch (error: Throwable) {
                _error.value = error
                _success.value = false
                _loading.value = false
            }
        }
    }

    fun getUserSessionData() : LiveData<Map<String, Any>> {
        viewModelScope.launch {
            _sessionData.value = authRepository.getSessionData()
        }
        return _sessionData
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getSuccess() : LiveData<Boolean> {
        return _success
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}