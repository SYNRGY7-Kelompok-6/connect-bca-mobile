package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _sessionTime: MutableLiveData<Long> = MutableLiveData<Long>()

    fun userLogin(userId: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                delay(2000)
//                val res = authRepository.userLogin(userId, password)
//                if (res) {
//                    _success.value = true
//                    getUserSessionTime()
//                }
                _success.value = true
                getUserSessionTime()
                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _success.value = false
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

    fun getUserSessionTime() : Long {
        var sessionTime: Long = 0
        viewModelScope.launch {
            sessionTime = authRepository.getSessionData().get("sessionTime") as Long
        }
        return sessionTime
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