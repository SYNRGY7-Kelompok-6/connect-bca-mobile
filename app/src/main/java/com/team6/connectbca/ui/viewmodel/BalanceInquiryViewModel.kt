package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.extensions.getCurrentDateString
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class BalanceInquiryViewModel(
    private val getBalanceInquiryUseCase: GetBalanceInquiryUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _balanceInquiry = MutableLiveData<AccountInfo?>()

    fun getBalanceInquiry() : LiveData<AccountInfo?> {
        val currentDate = getCurrentDateString()

        viewModelScope.launch {
            try {
                _loading.value = true
                val sessionData = getSessionDataUseCase()
                val token = sessionData.getValue("token") as String
                val data: AccountInfo? = getBalanceInquiryUseCase(
                    "Bearer $token",
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

        return  _balanceInquiry
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}
