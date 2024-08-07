package com.team6.connectbca.ui.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QrisPaymentViewModel : ViewModel() {
    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> get() = _amount

    private val _enteredAmount = StringBuilder()
    private val totalBalance = 2350000

    fun addDigit(digit: String) {
        _enteredAmount.append(digit)
        val enteredAmountInt = _enteredAmount.toString().toIntOrNull() ?: 0
        if (enteredAmountInt > totalBalance) {
            Log.d("QrisPaymentViewModel", "Balance is insufficient")
            _amount.value = _enteredAmount.toString()
        } else {
            _amount.value = _enteredAmount.toString()
        }
    }

    fun removeDigit() {
        if (_enteredAmount.length > 1) {
            _enteredAmount.deleteCharAt(_enteredAmount.length - 1)
            _amount.value = _enteredAmount.toString()
        }else if (_enteredAmount.length == 1){
            _enteredAmount.deleteCharAt(_enteredAmount.length - 1)
            _amount.value = "0"
        }else{
            _amount.value = "0"
        }
    }
}