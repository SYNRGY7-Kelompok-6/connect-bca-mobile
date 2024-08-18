package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.ShowQrUseCase
import com.team6.connectbca.extensions.getCurrentDateString
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class ShowQrViewModel(
    private val getBalanceInquiryUseCase: GetBalanceInquiryUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _success: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val qrImage: MutableLiveData<String> = MutableLiveData()
    private val _balanceInquiry = MutableLiveData<AccountInfo?>()

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




}