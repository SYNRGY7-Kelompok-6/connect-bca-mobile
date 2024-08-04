package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.accountmonthly.AccountMonthlyRemoteDataSource
import com.team6.connectbca.domain.model.AccountMonthly
import com.team6.connectbca.domain.repository.AccountMonthlyRepository

class AccountMonthlyRepositoryImpl(
    private val accountMonthlyRemoteDataSource: AccountMonthlyRemoteDataSource
) : AccountMonthlyRepository {
    override suspend fun getAccountMonthly(month: String): AccountMonthly {
        return accountMonthlyRemoteDataSource.getAccountMonthly(month).toEntity()
    }

}