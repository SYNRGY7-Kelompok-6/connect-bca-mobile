package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.model.QrisTransfer
import com.team6.connectbca.domain.model.ShowQr
import com.team6.connectbca.domain.model.ShowQrData
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.QrisTransferUseCase
import com.team6.connectbca.domain.usecase.ShowQrUseCase
import com.team6.connectbca.extensions.getCurrentDateString
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.UnsupportedOperationException

class QrisPaymentViewModel(
    private val getBalanceInquiryUseCase: GetBalanceInquiryUseCase,
    private val qrisTransferUseCase: QrisTransferUseCase,
    private val showQrUseCase: ShowQrUseCase,
) : ViewModel() {
    private val _amount = MutableLiveData<String>()
    val amount: LiveData<String> get() = _amount
    val transfer = MutableLiveData<Transfer?>()
    val qrData = MutableLiveData<ShowQrData?>()
    private val _enteredAmount = StringBuilder()
    private val totalBalance = 2350000

    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _balanceInquiry = MutableLiveData<AccountInfo?>()
    private val _transferSuccess = MutableLiveData<Boolean>()
    val transferSuccess: LiveData<Boolean> get() = _transferSuccess
    private val _showQrSuccess = MutableLiveData<Boolean>()
    val showQrSuccess: LiveData<Boolean> get() = _showQrSuccess
    private val _transferError = MutableLiveData<Throwable>()
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
        } else if (_enteredAmount.length == 1) {
            _enteredAmount.deleteCharAt(_enteredAmount.length - 1)
            _amount.value = "0"
        } else {
            _amount.value = "0"
        }
    }

    fun getBalanceInquiry(): LiveData<AccountInfo?> {
        val currentDate = getCurrentDateString()

        viewModelScope.launch {
            try {
                _loading.value = true
                val data: AccountInfo? = getBalanceInquiryUseCase(
                    currentDate,
                    currentDate,
                    "0",
                    "1"
                )

                if (data != null) {
                    _balanceInquiry.value = data
                } else {
                    _error.value = UnsupportedOperationException("Failed to load data")
                }
                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return _balanceInquiry
    }

    fun qrisTransfer(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ) {
        Log.d(
            "QrisPaymentViewModel",
            "Beneficiary account number: $beneficiaryAccountNumber, remark: $remark, desc: $desc, amount: $amountValue, currency: $currency"
        )
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d("QrisPaymentViewModel", "Transfer initiated")
                var response = qrisTransferUseCase(
                    pinToken,
                    beneficiaryAccountNumber,
                    remark,
                    desc,
                    amountValue,
                    currency
                )
                Log.d("QrisPaymentViewModel", "Transfer response: $response")

                if (response?.status == "success") {
                    Log.d("QrisPaymentViewModel", "Transfer success")
                    transfer.value = response
                    _transferSuccess.value = true
                } else {
                    Log.e("QrisPaymentViewModel", "ERROR BAJINGANNNNN")
                    // Log the response message and set error state
                    Log.e("QrisPaymentViewModel", "Transfer failed: ${response?.message}")
                    _transferError.value = response?.message?.let { Throwable(it) }
                    _transferSuccess.value = false
                }
            } catch (e: Exception) {
                // Log the exception for debugging
                Log.e("QrisPaymentViewModel", "Exception during transfer: ${e.message}", e)
                _transferError.value = e
                _transferSuccess.value = false
            }
            _loading.value = false
        }
    }

    fun showQrTransfer(
        amountValue: Double,
        currency: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                var response = showQrUseCase(amountValue, currency)
                Log.d("QrisPaymentViewModel", "Show QR response: $response")
                if (response?.status == "Success") {
                    qrData.value = response.data
                    _showQrSuccess.value = true
                } else {
                    Log.e("QrisPaymentViewModel", "Show QR failed: ${response?.message}")
                    _showQrSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("QrisPaymentViewModel", "Exception during showQr: ${e.message}", e)
            }
            _loading.value = false
        }
    }

    fun getLoading(): LiveData<Boolean> {
        return _loading
    }
}