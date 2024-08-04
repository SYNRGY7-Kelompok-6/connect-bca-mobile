package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.model.AccountMonthlyData
import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.usecase.GetAccountMonthlyUseCase
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.extensions.getCurrentDateString
import com.team6.connectbca.extensions.getNonZeroMonth
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class BalanceInquiryViewModel(
    private val getBalanceInquiryUseCase: GetBalanceInquiryUseCase,
    private val getAccountMonthlyUseCase: GetAccountMonthlyUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _balanceInquiry = MutableLiveData<AccountInfo?>()
    private val _accountMonthly = MutableLiveData<AccountMonthlyData?>()
    val month = getNonZeroMonth()

    fun getBalanceInquiry() : LiveData<AccountInfo?> {
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

        return  _balanceInquiry
    }

    fun getAccountMonthly() : LiveData<AccountMonthlyData?> {
        viewModelScope.launch {
            try {
                _loading.value = true
                val data: AccountMonthlyData? = getAccountMonthlyUseCase(month)
                if (data != null) {
                    _accountMonthly.value = data
                } else {
                    _error.value = UnsupportedOperationException("Failed to load data")
                }
                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return  _accountMonthly
    }

    fun getMonthName() : LiveData<String> {
        val _result : MutableLiveData<String> = MutableLiveData<String>()
        var result: String = when(month) {
            "1" -> "Bulan Januari"
            "2" -> "Bulan Februari"
            "3" -> "Bulan Maret"
            "4" -> "Bulan April"
            "5" -> "Bulan Mei"
            "6" -> "Bulan Juni"
            "7" -> "Bulan Juli"
            "8" -> "Bulan Agustus"
            "9" -> "Bulan September"
            "10" -> "Bulan Oktober"
            "11" -> "Bulan November"
            else -> "Bulan Desember"
        }

        _result.value = result

        return _result
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}