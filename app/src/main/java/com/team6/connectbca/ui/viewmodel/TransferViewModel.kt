package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.TransactionAmount
import com.team6.connectbca.domain.usecase.TransferUseCase
import kotlinx.coroutines.launch

class TransferViewModel(private val transferUseCase: TransferUseCase) : ViewModel() {

    private val _transferResult = MutableLiveData<TransferUseCase.TransferInfo>()
    val transferResult: LiveData<TransferUseCase.TransferInfo> get() = _transferResult

    fun initiateTransfer(
        token: String,
        beneficiaryAccountNumber: String,
        remark: String,
        desc: String,
        amount: TransactionAmount
    ) {
        viewModelScope.launch {
            val result = transferUseCase.execute(token, beneficiaryAccountNumber, remark, desc, amount)
            _transferResult.postValue(result)
        }
    }
}