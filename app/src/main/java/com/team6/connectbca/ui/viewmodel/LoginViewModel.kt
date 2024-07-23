package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.repository.AuthRepository
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
                val responseMessage = authRepository.userLogin(userId, password)
                authRepository.saveSessionData(userId, password)
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
            _sessionTime.value = authRepository.getSessionData()["sessionTime"] as Long
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