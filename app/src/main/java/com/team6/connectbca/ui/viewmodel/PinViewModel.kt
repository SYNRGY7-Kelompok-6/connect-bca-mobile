package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PinViewModel : ViewModel() {

    private val _pinLength = MutableLiveData<Int>()
    val pinLength: LiveData<Int> get() = _pinLength

    private val _pinError = MutableLiveData<Boolean>()
    val pinError: LiveData<Boolean> get() = _pinError

    private val correctPin = "123456"

    private val _enteredPin = StringBuilder()

    init {
        _pinLength.value = 0
        _pinError.value = false
    }

    fun addDigit(digit: String) {
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

    private fun validatePin() {
        if (_enteredPin.toString() == correctPin) {
            _pinError.value = false
        } else {
            _pinError.value = true
            _enteredPin.clear()
            _pinLength.value = _enteredPin.length
        }
    }
}