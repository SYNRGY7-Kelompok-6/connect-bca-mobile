package com.team6.connectbca.diviewmodel

import com.team6.connectbca.ui.viewmodel.BankStatementViewModel
import com.team6.connectbca.ui.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(authRepository = get()) }
    viewModel { BankStatementViewModel(get()) }
}