package com.team6.connectbca.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.GetMutationUseCase
import com.team6.connectbca.extensions.getCurrentDateString
import kotlinx.coroutines.launch
import java.lang.UnsupportedOperationException

class TodayMutationViewModel(
    private val getMutationUseCase: GetMutationUseCase,
    private val getSessionDataUseCase: GetSessionDataUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _todayMutation = MutableLiveData<List<MutationsItem?>?>()

    fun getTodayMutation() : LiveData<List<MutationsItem?>?> {
        val currentDate = getCurrentDateString()

        viewModelScope.launch {
            try {
                _loading.value = true
                val sessionData = getSessionDataUseCase()
                val token = sessionData.getValue("token") as String
                val data: List<MutationsItem?>? = getMutationUseCase(
                    "Bearer $token",
                    currentDate,
                    currentDate,
                    "0",
                    "100"
                )

                if (!data.isNullOrEmpty()) {
                    _todayMutation.value = data
                } else {
                    _todayMutation.value = null
                }

                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return  _todayMutation
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}