package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.usecase.GetDateRangeMutationUseCase
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import kotlinx.coroutines.launch

class SearchMutationViewModel(
    private val getDateRangeMutationUseCase: GetDateRangeMutationUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _searchMutation = MutableLiveData<List<DailyTransaction>?>()

    fun getSearchedMutation(startDate: String, endDate: String) : LiveData<List<DailyTransaction>?> {
        viewModelScope.launch {
            try {
                _loading.value = true
                val sessionData = getSessionDataUseCase()
                val token = sessionData.getValue("token") as String
                val data: List<DailyTransaction>? = getDateRangeMutationUseCase(
                    "Bearer $token",
                    startDate,
                    endDate,
                    "0",
                    "500"
                )

                if (data != null) {
                    _searchMutation.value = data
                } else {
                    _searchMutation.value = null
                }

                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return  _searchMutation
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}