package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.GetThisMonthMutationUseCase
import com.team6.connectbca.extensions.getEndOfMonthDate
import com.team6.connectbca.extensions.getFirstOfMonthDate
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class MonthMutationViewModel(
    private val getThisMonthMutationUseCase: GetThisMonthMutationUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _monthMutation = MutableLiveData<List<DailyTransaction>?>()

    fun getThisMonthMutation() : LiveData<List<DailyTransaction>?> {
        val firstDate = getFirstOfMonthDate()
        val endDate = getEndOfMonthDate()

        viewModelScope.launch {
            try {
                _loading.value = true
                val sessionData = getSessionDataUseCase()
                val token = sessionData.getValue("token") as String
                val data = getThisMonthMutationUseCase(
                    "Bearer $token",
                    firstDate,
                    endDate,
                    "0",
                    "500"
                )

                if (data != null) {
                    _monthMutation.value = data
                }

                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return  _monthMutation
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}