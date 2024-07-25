package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.usecase.GetBankStatementUseCase
import com.team6.connectbca.data.model.bankstatement.BankStatementResponse
import kotlinx.coroutines.launch

class BankStatementViewModel(private val getBankStatementUseCase: GetBankStatementUseCase) : ViewModel() {
    private val _bankStatementResponse = MutableLiveData<BankStatementResponse>()
    val bankStatementResponse: LiveData<BankStatementResponse> get() = _bankStatementResponse

    fun fetchBankStatement(fromDate: String, toDate: String) {
        viewModelScope.launch {
            try {
                val response = getBankStatementUseCase.execute(fromDate, toDate)
                _bankStatementResponse.value = response
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}
