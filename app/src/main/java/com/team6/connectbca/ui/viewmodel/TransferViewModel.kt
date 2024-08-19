package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.TransactionAmount
import com.team6.connectbca.domain.model.TransactionDetailData
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
    private val _transferResult = MutableLiveData<TransferUseCase.TransferInfo>()

    fun getTransactionDetail(transactionId: String) : LiveData<TransactionDetailData?> {
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

    fun initiateTransfer(
        token: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amount: TransactionAmount
    ) : LiveData<TransferUseCase.TransferInfo> {
        viewModelScope.launch {
            val result = transferUseCase(token, beneficiaryAccountNumber, remark, desc, amount)
            _transferResult.postValue(result)
        }

        return _transferResult
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}