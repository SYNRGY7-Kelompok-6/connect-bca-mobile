package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.GetMutationUseCase
import com.team6.connectbca.extensions.getEndOfMonthDate
import com.team6.connectbca.extensions.getFirstOfMonthDate
import kotlinx.coroutines.launch

class MonthMutationViewModel(
    private val getMutationUseCase: GetMutationUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _monthMutation = MutableLiveData<List<MutationsItem?>>()

    fun getThisMonthMutation() : LiveData<List<MutationsItem?>> {
        val firstDate = getFirstOfMonthDate()
        val endDate = getEndOfMonthDate()

        viewModelScope.launch {
            try {
                _loading.value = true
                val sessionData = getSessionDataUseCase()
                val token = sessionData.getValue("token") as String
                val data = getMutationUseCase(
                    token,
                    firstDate,
                    endDate,
                    "0",
                    "1"
                )
                _monthMutation.value = data
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