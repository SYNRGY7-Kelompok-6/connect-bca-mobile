package com.team6.connectbca.diviewmodel

import com.team6.connectbca.ui.viewmodel.AuthViewModel
import com.team6.connectbca.ui.viewmodel.BalanceInquiryViewModel
import com.team6.connectbca.ui.viewmodel.HomeViewModel
import com.team6.connectbca.ui.viewmodel.InputTransferAmountViewModel
import com.team6.connectbca.ui.viewmodel.MonthMutationViewModel
import com.team6.connectbca.ui.viewmodel.PinViewModel
import com.team6.connectbca.ui.viewmodel.ProfileViewModel
import com.team6.connectbca.ui.viewmodel.QrisPaymentViewModel
import com.team6.connectbca.ui.viewmodel.QrisViewModel
import com.team6.connectbca.ui.viewmodel.SavedAccountViewModel
import com.team6.connectbca.ui.viewmodel.SearchMutationViewModel
import com.team6.connectbca.ui.viewmodel.ShowQrViewModel
import com.team6.connectbca.ui.viewmodel.TodayMutationViewModel
import com.team6.connectbca.ui.viewmodel.TransferViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            loginUseCase = get(),
            logoutUseCase = get(),
            getSessionDataUseCase = get()
        )
    }
    viewModel {
        BalanceInquiryViewModel(
            getBalanceInquiryUseCase = get(),
            getAccountMonthlyUseCase = get()
        )
    }
    viewModel { TodayMutationViewModel(getMutationUseCase = get()) }
    viewModel { MonthMutationViewModel(getThisMonthMutationUseCase = get()) }
    viewModel { SearchMutationViewModel(getDateRangeMutationUseCase = get()) }
    viewModel { QrisViewModel(qrisVerifyUseCase = get(), getSessionDataUseCase = get()) }
    viewModel { TransferViewModel(getTransactionDetailUseCase = get(), transferUseCase = get()) }
    viewModel { SavedAccountViewModel(get(),get(),get(),get() ) }
    viewModel {
        QrisPaymentViewModel(
            getBalanceInquiryUseCase = get(),
            qrisTransferUseCase = get()
        )
    }
    viewModel { ShowQrViewModel(getBalanceInquiryUseCase = get(), showQrUseCase = get(), latestTransactionUseCase = get()) }
    viewModel { PinViewModel(pinValidationUseCase = get()) }
    viewModel { HomeViewModel(getBalanceInquiryUseCase = get(), logoutUseCase = get()) }
    viewModel { InputTransferAmountViewModel(getBalanceInquiryUseCase = get()) }
    viewModel { ProfileViewModel(getUserProfileUseCase = get(), updateUserProfileUseCase = get()) }
}
