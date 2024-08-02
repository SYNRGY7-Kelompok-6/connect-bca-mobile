package com.team6.connectbca.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.qris.QrisRemoteDataSource
import com.team6.connectbca.data.datasource.local.AuthLocalDataSourceImpl
import com.team6.connectbca.data.datasource.remote.AuthRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.BankStatementRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.QrisRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.services.BankStatementService
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.datasource.services.QrisService
import com.team6.connectbca.data.datasource.services.datastore
import com.team6.connectbca.data.datasource.services.provideBankStatementService
import com.team6.connectbca.data.datasource.services.provideLoginService
import com.team6.connectbca.data.datasource.services.provideQrisService
import com.team6.connectbca.data.repository.AuthRepositoryImpl
import com.team6.connectbca.data.repository.BankStatementRepositoryImpl
import com.team6.connectbca.data.repository.QrisRepositoryImpl
import com.team6.connectbca.domain.repository.AuthRepository
import com.team6.connectbca.domain.repository.BankStatementRepository
import com.team6.connectbca.domain.repository.QrisRepository
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.GetMutationUseCase
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import com.team6.connectbca.domain.usecase.LoginUseCase
import com.team6.connectbca.domain.usecase.LogoutUseCase
import com.team6.connectbca.domain.usecase.QrVerifyUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {
    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(authLocalDataSource = get(), authRemoteDataSource = get()) }
    single<BankStatementRepository> { BankStatementRepositoryImpl(remoteDataSource = get()) }
    single<QrisRepository> { QrisRepositoryImpl(qrisRemoteDataSource = get())}

    // Data Sources
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(dataStore = get()) }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(loginService = get()) }
    single<BankStatementRemoteDataSource> { BankStatementRemoteDataSourceImpl(bankStatementService = get()) }
    single<QrisRemoteDataSource> { QrisRemoteDataSourceImpl(qrisService = get()) }

    // Data Store
    single<DataStore<Preferences>> { androidContext().datastore }

    // Services
    single<LoginService> { provideLoginService(androidContext(), get()) }
    single<BankStatementService> { provideBankStatementService(androidContext(), get()) }
    single<QrisService> { provideQrisService(androidContext(), get()) }

    // Use Cases
    single { LoginUseCase(authRepository = get()) }
    single { LogoutUseCase(authRepository = get()) }
    single { GetBalanceInquiryUseCase(bankStatementRepository = get()) }
    single { GetMutationUseCase(bankStatementRepository = get()) }
    single { GetSessionDataUseCase(authRepository = get()) }
    single { QrVerifyUseCase(qrisRepository = get())}
}