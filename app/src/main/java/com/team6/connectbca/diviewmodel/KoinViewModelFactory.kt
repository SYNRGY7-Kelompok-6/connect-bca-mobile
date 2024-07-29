package com.team6.connectbca.diviewmodel

import com.team6.connectbca.ui.viewmodel.BalanceInquiryViewModel
import com.team6.connectbca.ui.viewmodel.AuthViewModel
import com.team6.connectbca.ui.viewmodel.MonthMutationViewModel
import com.team6.connectbca.ui.viewmodel.TodayMutationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(loginUseCase = get(), logoutUseCase = get(), getSessionDataUseCase = get()) }
    viewModel { BalanceInquiryViewModel(getBalanceInquiryUseCase = get(), getSessionDataUseCase = get()) }
    viewModel { TodayMutationViewModel(getMutationUseCase = get(), getSessionDataUseCase = get()) }
    viewModel { MonthMutationViewModel(getMutationUseCase = get(), getSessionDataUseCase = get()) }
}