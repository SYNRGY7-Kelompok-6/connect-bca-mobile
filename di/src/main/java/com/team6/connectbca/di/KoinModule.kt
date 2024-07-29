package com.team6.connectbca.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import com.team6.connectbca.data.datasource.interfaces.auth.AuthRemoteDataSource
import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.data.datasource.local.AuthLocalDataSourceImpl
import com.team6.connectbca.data.datasource.remote.AuthRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.remote.BankStatementRemoteDataSourceImpl
import com.team6.connectbca.data.datasource.services.BankStatementService
import com.team6.connectbca.data.datasource.services.LoginService
import com.team6.connectbca.data.datasource.services.datastore
import com.team6.connectbca.data.datasource.services.provideBankStatementService
import com.team6.connectbca.data.datasource.services.provideLoginService
import com.team6.connectbca.data.repository.AuthRepositoryImpl
import com.team6.connectbca.data.repository.BankStatementRepositoryImpl
import com.team6.connectbca.domain.repository.AuthRepository
import com.team6.connectbca.domain.repository.BankStatementRepository
import com.team6.connectbca.domain.usecase.GetBalanceInquiryUseCase
import com.team6.connectbca.domain.usecase.GetMutationUseCase
import com.team6.connectbca.domain.usecase.GetSessionDataUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {
    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(authLocalDataSource = get(), authRemoteDataSource = get()) }
    single<BankStatementRepository> { BankStatementRepositoryImpl(remoteDataSource = get()) }

    // Data Sources
    single<AuthLocalDataSource> { AuthLocalDataSourceImpl(dataStore = get()) }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(loginService = get()) }
    single<BankStatementRemoteDataSource> { BankStatementRemoteDataSourceImpl(bankStatementService = get()) }

    // Data Store
    single<DataStore<Preferences>> { androidContext().datastore }

    // Services
    single<LoginService> { provideLoginService(androidContext()) }
    single<BankStatementService> { provideBankStatementService(androidContext()) }

    // Use Cases
    single { GetBalanceInquiryUseCase(bankStatementRepository = get()) }
    single { GetMutationUseCase(bankStatementRepository = get()) }
    single { GetSessionDataUseCase(authRepository = get()) }
}