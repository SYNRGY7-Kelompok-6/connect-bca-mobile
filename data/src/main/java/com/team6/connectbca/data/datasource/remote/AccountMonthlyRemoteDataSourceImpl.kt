package com.team6.connectbca.data.datasource.remote

import com.team6.connectbca.data.datasource.interfaces.accountmonthly.AccountMonthlyRemoteDataSource
import com.team6.connectbca.data.datasource.services.AccountMonthlyService
import com.team6.connectbca.data.model.response.AccountMonthlyResponse

class AccountMonthlyRemoteDataSourceImpl(
    private val accountMonthlyService: AccountMonthlyService
) : AccountMonthlyRemoteDataSource {
    override suspend fun getAccountMonthly(month: String): AccountMonthlyResponse {
        return accountMonthlyService.getAccountMonthly(month)
    }

}