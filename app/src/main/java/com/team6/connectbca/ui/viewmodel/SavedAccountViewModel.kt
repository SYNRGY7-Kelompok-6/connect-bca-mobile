package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.SavedAccount
import com.team6.connectbca.domain.model.SavedAccounts
import com.team6.connectbca.domain.usecase.GetSavedAccountDetailUseCase
import com.team6.connectbca.domain.usecase.GetSavedAccountsUseCase
import com.team6.connectbca.domain.usecase.SaveAccountUseCase
import com.team6.connectbca.domain.usecase.UpdateFavoriteUseCase
import kotlinx.coroutines.launch

class SavedAccountViewModel(
    private val getSavedAccountsUseCase: GetSavedAccountsUseCase,
    private val saveAccountUseCase: SaveAccountUseCase,
    private val getSavedAccountDetailUseCase: GetSavedAccountDetailUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Throwable>()
    private val _savedAccounts = MutableLiveData<SavedAccounts>()
    private val _saveAccountResult = MutableLiveData<SaveAccountUseCase.SaveAccountInfo>()
    private val _savedAccountDetail = MutableLiveData<SavedAccount>()
    private val _updateFavoriteResult = MutableLiveData<UpdateFavoriteUseCase.UpdateFavoriteInfo>()

    fun getSavedAccounts(q: String, isFavorite: Boolean): LiveData<SavedAccounts> {
        performTaskWithLoading {
            val accounts = getSavedAccountsUseCase(q, isFavorite)
            _savedAccounts.postValue(accounts)
        }
        return _savedAccounts
    }

    fun saveAccount(beneficiaryAccountNumber: String): LiveData<SaveAccountUseCase.SaveAccountInfo> {
        performTask {
            val result = saveAccountUseCase(beneficiaryAccountNumber)
            _saveAccountResult.postValue(result)
        }
        return _saveAccountResult
    }

    fun getSavedAccountDetail(savedBeneficiaryId: String): LiveData<SavedAccount> {
        performTaskWithLoading {
            val account = getSavedAccountDetailUseCase(savedBeneficiaryId)
            _savedAccountDetail.postValue(account)
        }
        return _savedAccountDetail
    }

    fun updateFavorite(
        savedBeneficiaryId: String,
        isFavorite: Boolean
    ): LiveData<UpdateFavoriteUseCase.UpdateFavoriteInfo> {
        performTask {
            val result = updateFavoriteUseCase(savedBeneficiaryId, isFavorite)
            _updateFavoriteResult.postValue(result)
        }
        return _updateFavoriteResult
    }

    private fun performTask(task: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                task()
            } catch (error: Throwable) {
                _error.postValue(error)
            }
        }
    }

    private fun performTaskWithLoading(task: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.postValue(true)
                task()
            } catch (error: Throwable) {
                _error.postValue(error)
            } finally {
                _loading.postValue(false)
            }
        }
    }

    fun getError(): LiveData<Throwable> = _error
    fun getLoading(): LiveData<Boolean> = _loading
}