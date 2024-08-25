package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.TransactionDetailData
import com.team6.connectbca.domain.model.Transfer
import com.team6.connectbca.domain.usecase.GetTransactionDetailUseCase
import com.team6.connectbca.domain.usecase.TransferUseCase
import kotlinx.coroutines.launch

class TransferViewModel(
    private val getTransactionDetailUseCase: GetTransactionDetailUseCase,
    private val transferUseCase: TransferUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _transactionDetail = MutableLiveData<TransactionDetailData?>()
    val transferResult = MutableLiveData<Transfer?>()
    private val _transferSuccess = MutableLiveData<Boolean>()
    private val _transferError = MutableLiveData<Throwable>()
    val transferSuccess: LiveData<Boolean> get() = _transferSuccess

    fun getTransactionDetail(transactionId: String): LiveData<TransactionDetailData?> {
        viewModelScope.launch {
            try {
                _loading.value = true
                val data: TransactionDetailData? = getTransactionDetailUseCase(transactionId)

                if (data != null) {
                    _transactionDetail.value = data
                } else {
                    _transactionDetail.value = null
                }

                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return _transactionDetail
    }

    fun transferInitiate(
        pinToken: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amountValue: Double,
        currency: String
    ) {
        Log.d("TransferViewModel", "$pinToken, $beneficiaryAccountNumber, $remark, $desc, $amountValue, $currency")
        viewModelScope.launch {
            _loading.value = true
            Log.d("TransferViewModel", "Transfer initiated")
            try {
                var response = transferUseCase(
                    pinToken,
                    beneficiaryAccountNumber,
                    remark,
                    desc,
                    amountValue,
                    currency
                )

                if (response?.status == "success") {
                    Log.d("QrisPaymentViewModel", "Transfer success")
                    transferResult.value = response
                    _transferSuccess.value = true
                } else {
                    // Log the response message and set error state
                    Log.e("QrisPaymentViewModel", "Transfer failed: ${response}")
                    _transferError.value = response?.message?.let { Throwable(it) }
                    _transferSuccess.value = false
                }
            } catch (e: Exception) {
                // Log the exception for debugging
                Log.e("QrisPaymentViewModel", "Exception during transfer: ${e.message}", e)
                e.printStackTrace()
                _transferError.value = e
                _transferSuccess.value = false
            }
            _loading.value = false
        }
    }

    fun getError(): LiveData<Throwable> {
        return _error
    }

    fun getLoading(): LiveData<Boolean> {
        return _loading
    }
}