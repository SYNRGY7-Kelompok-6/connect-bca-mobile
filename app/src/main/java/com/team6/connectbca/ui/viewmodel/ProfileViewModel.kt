package com.team6.connectbca.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.connectbca.domain.model.UserProfileData
import com.team6.connectbca.domain.usecase.GetUserProfileUseCase
import com.team6.connectbca.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val _error: MutableLiveData<Throwable> = MutableLiveData<Throwable>()
    private val _userProfile = MutableLiveData<UserProfileData?>()

    fun getUserProfile() : LiveData<UserProfileData?> {
        viewModelScope.launch {
            try {
                _loading.value = true
                val data: UserProfileData? = getUserProfileUseCase()

                if (data != null) {
                    _userProfile.value = data
                } else {
                    _userProfile.value = null
                }

                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return  _userProfile
    }

    fun updateUserProfile(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        birth: String? = null,
        address: String? = null,
        image: File? = null
    ) : LiveData<UserProfileData?> {
        viewModelScope.launch {
            try {
                _loading.value = true
                val data: UserProfileData? = updateUserProfileUseCase(
                    name, email, phone, birth, address, image
                )

                if (data != null) {
                    _userProfile.value = data
                } else {
                    _userProfile.value = null
                }

                _loading.value = false
            } catch (error: Throwable) {
                _error.value = error
                _loading.value = false
            }
        }

        return  _userProfile
    }

    fun getError() : LiveData<Throwable> {
        return _error
    }

    fun getLoading() : LiveData<Boolean> {
        return _loading
    }
}