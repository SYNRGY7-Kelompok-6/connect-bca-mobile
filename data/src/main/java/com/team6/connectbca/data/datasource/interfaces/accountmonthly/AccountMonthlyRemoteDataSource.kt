package com.team6.connectbca.data.datasource.interfaces.accountmonthly

import com.team6.connectbca.data.model.response.AccountMonthlyResponse

interface AccountMonthlyRemoteDataSource {
    suspend fun getAccountMonthly(month: String) : AccountMonthlyResponse
}