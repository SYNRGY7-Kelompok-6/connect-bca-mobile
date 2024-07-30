package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.LoginUseCase
import com.team6.connectbca.domain.usecase.LogoutUseCase
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _sessionTime: MutableLiveData<Long> = MutableLiveData<Long>()

    fun userLogin(userId: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val res = loginUseCase(userId, password)
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
                logoutUseCase()
                _loading.value = false
                _success.value = true
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }
    }

    fun getUserSessionTime() : LiveData<Long> {
        viewModelScope.launch {
            try {
                _sessionData.value = getSessionDataUseCase()!!
            } catch (error: Throwable) {
                _error.value = error
            }
        }

        return _sessionTime
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