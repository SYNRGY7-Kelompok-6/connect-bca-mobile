package com.team6.connectbca.ui.viewmodel

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team6.connectbca.domain.usecase.PinValidationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PinViewModel(
    private val pinValidationUseCase: PinValidationUseCase
) : ViewModel() {

    private val _pinLength = MutableLiveData<Int>()
    val pinLength: LiveData<Int> get() = _pinLength
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading
    private val _pinError = MutableLiveData<Boolean>()
    val pinError: LiveData<Boolean> get() = _pinError

    private val correctPin = "123456"
    val pinToken = MutableLiveData<String?>()
    private val _enteredPin = StringBuilder()

    init {
        _pinLength.value = 0
        _pinError.value = false
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    suspend fun addDigit(digit: String) {
        if (_enteredPin.length < 6) {
            _enteredPin.append(digit)
            _pinLength.value = _enteredPin.length
            if (_enteredPin.length == 6) {
                validatePin()
            }
        }
    }

    fun removeDigit() {
        if (_enteredPin.isNotEmpty()) {
            _enteredPin.deleteCharAt(_enteredPin.length - 1)
            _pinLength.value = _enteredPin.length
            _pinError.value = false
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private suspend fun validatePin() {
        Log.d("PinViewModel", "$_enteredPin")
        try {
            _loading.value = true
            val response = withContext(Dispatchers.IO) {
                pinValidationUseCase(_enteredPin.toString())
            }
            if (response.status == "success") {
                pinToken.value = response.data?.pinToken
                _pinError.value = false
            } else {
                _pinError.value = true
                _enteredPin.clear()
                _pinLength.value = _enteredPin.length
            }
        } catch (e: HttpException) {
            _pinError.value = true
            e.printStackTrace()
            _enteredPin.clear()
            _pinLength.value = _enteredPin.length
        } catch (e: Exception) {
            _pinError.value = true
            _enteredPin.clear()
            e.printStackTrace()
            _pinLength.value = _enteredPin.length
        }
        _loading.value = false
    }


}