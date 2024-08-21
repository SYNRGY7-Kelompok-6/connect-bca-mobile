package com.team6.connectbca.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.team6.connectbca.data.datasource.interfaces.accountmonthly.AccountMonthlyRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.qris.QrisRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.savedaccount.SavedAccountRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.transfer.TransferRemoteDataSource
import com.team6.connectbca.data.datasource.local.AuthLocalDataSourceImpl
import com.team6.connectbca.data.datasource.remote.AccountMonthlyRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.AuthRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.BankStatementRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.services.AccountMonthlyService
import com.team6.connectbca.data.datasource.remote.QrisRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.SavedAccountRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.TransferRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.services.BankStatementService
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.datasource.services.PinService
import com.team6.connectbca.data.datasource.services.QrisService
import com.team6.connectbca.data.datasource.services.SavedAccountService
import com.team6.connectbca.data.datasource.services.TransferService
import com.team6.connectbca.data.datasource.services.datastore
import com.team6.connectbca.data.datasource.services.provideAccountMonthlyService
import com.team6.connectbca.data.datasource.services.provideBankStatementService
import com.team6.connectbca.data.datasource.services.provideLoginService
import com.team6.connectbca.data.datasource.services.providePinService
import com.team6.connectbca.data.repository.AccountMonthlyRepositoryImpl
import com.team6.connectbca.data.repository.AuthRepositoryImpl
import com.team6.connectbca.data.repository.BankStatementRepositoryImpl
import com.team6.connectbca.domain.repository.AccountMonthlyRepository
import com.team6.connectbca.domain.repository.AuthRepository
import com.team6.connectbca.domain.repository.BankStatementRepository
import com.team6.connectbca.domain.usecase.GetAccountMonthlyUseCase
import com.team6.connectbca.data.datasource.services.provideQrisService
import com.team6.connectbca.data.datasource.services.provideSavedAccountService
import com.team6.connectbca.data.datasource.services.provideTransferService
import com.team6.connectbca.data.repository.QrisRepositoryImpl
import com.team6.connectbca.data.repository.SavedAccountRepositoryImpl
import com.team6.connectbca.data.repository.TransferRepositoryImpl
import com.team6.connectbca.domain.repository.QrisRepository
import com.team6.connectbca.domain.repository.SavedAccountRepository
import com.team6.connectbca.domain.repository.TransferRepository
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.GetDateRangeMutationUseCase
import com.team6.connectbca.domain.usecase.GetLatestTransactionUseCase
import com.team6.connectbca.domain.usecase.GetMutationUseCase
import com.team6.connectbca.domain.usecase.GetSavedAccountDetailUseCase
import com.team6.connectbca.domain.usecase.GetSavedAccountsUseCase
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.GetThisMonthMutationUseCase
import com.team6.connectbca.domain.usecase.GetTransactionDetailUseCase
import com.team6.connectbca.domain.usecase.LoginUseCase
import com.team6.connectbca.domain.usecase.LogoutUseCase
import com.team6.connectbca.domain.usecase.PinValidationUseCase
import com.team6.connectbca.domain.usecase.QrVerifyUseCase
import com.team6.connectbca.domain.usecase.SaveAccountUseCase
import com.team6.connectbca.domain.usecase.TransferUseCase
import com.team6.connectbca.domain.usecase.UpdateFavoriteUseCase
import com.team6.connectbca.domain.usecase.QrisTransferUseCase
import com.team6.connectbca.domain.usecase.ShowQrUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {
    // Repositories
    single<AuthRepository> {
        AuthRepositoryImpl(
            authLocalDataSource = get(),
            authRemoteDataSource = get()
        )
    }
    single<BankStatementRepository> { BankStatementRepositoryImpl(remoteDataSource = get()) }
    single<AccountMonthlyRepository> { AccountMonthlyRepositoryImpl(accountMonthlyRemoteDataSource = get()) }
    single<QrisRepository> { QrisRepositoryImpl(qrisRemoteDataSource = get()) }
    single<TransferRepository> { TransferRepositoryImpl(transferRemoteDataSource = get()) }
    single<SavedAccountRepository> { SavedAccountRepositoryImpl(savedAccountRemoteDataSource = get()) }

    // Data Sources
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(dataStore = get()) }
    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(
            loginService = get(),
            pinService = get()
        )
    }
    single<BankStatementRemoteDataSource> { BankStatementRemoteDataSourceImpl(bankStatementService = get()) }
    single<AccountMonthlyRemoteDataSource> {
        AccountMonthlyRemoteDataSourceImpl(
            accountMonthlyService = get()
        )
    }
    single<QrisRemoteDataSource> { QrisRemoteDataSourceImpl(qrisService = get()) }
    single<TransferRemoteDataSource> { TransferRemoteDataSourceImpl(transferService = get()) }
    single<SavedAccountRemoteDataSource> {SavedAccountRemoteDataSourceImpl(savedAccountService = get()) }

    // Data Store
    single<DataStore<Preferences>> { androidContext().datastore }

    // Services
    single<LoginService> { provideLoginService(androidContext()) }
    single<BankStatementService> { provideBankStatementService(androidContext(), get()) }
    single<AccountMonthlyService> { provideAccountMonthlyService(androidContext(), get()) }
    single<QrisService> { provideQrisService(androidContext(), get()) }
    single<TransferService> { provideTransferService(androidContext(), get()) }
    single<SavedAccountService> { provideSavedAccountService(androidContext(), get()) }
    single<PinService> { providePinService(androidContext(), get()) }

    // Use Cases
    single { LoginUseCase(authRepository = get()) }
    single { LogoutUseCase(authRepository = get()) }
    single { GetBalanceInquiryUseCase(bankStatementRepository = get()) }
    single { GetMutationUseCase(bankStatementRepository = get()) }
    single { GetSessionDataUseCase(authRepository = get()) }
    single { GetThisMonthMutationUseCase(bankStatementRepository = get()) }
    single { GetAccountMonthlyUseCase(accountMonthlyRepository = get()) }
    single { GetDateRangeMutationUseCase(bankStatementRepository = get()) }
    single { QrVerifyUseCase(qrisRepository = get()) }
    single { ShowQrUseCase(qrisRepository = get()) }
    single { GetLatestTransactionUseCase(bankStatementRepository = get()) }
    single { GetTransactionDetailUseCase(transferRepository = get()) }
    single { TransferUseCase(transferRepository = get()) }
    single { SaveAccountUseCase(get()) }
    single { GetSavedAccountsUseCase(savedAccountRepository = get()) }
    single { GetSavedAccountDetailUseCase(savedAccountRepository = get()) }
    single { UpdateFavoriteUseCase(savedAccountRepository = get()) }
    single { PinValidationUseCase(authRepository = get()) }
    single { QrisTransferUseCase(transferRepository = get()) }

}