package com.team6.connectbca.data.repository

import com.team6.connectbca.data.datasource.interfaces.bankstatement.BankStatementRemoteDataSource
import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.repository.BankStatementRepository
import java.text.SimpleDateFormat
import java.util.Locale

class BankStatementRepositoryImpl(
    private val remoteDataSource: BankStatementRemoteDataSource
) : BankStatementRepository {
    override suspend fun getBankStatement(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ) : BankStatementData? {
        return remoteDataSource.getBankStatement(token, fromDate, toDate, page, pageSize).toEntity().data
    }

    override suspend fun getTransactionGroups(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): List<DailyTransaction>? {
        val allMutations = remoteDataSource.getBankStatement(
            token, fromDate, toDate, page, pageSize).toEntity().data?.mutations
        val result : MutableList<DailyTransaction> = mutableListOf()
        var temp: MutableList<MutationsItem> = mutableListOf()
        var count = 0

        if (allMutations != null) {
            var dateFlag = reformatDate(allMutations.firstOrNull()?.transactionDate!!)

            allMutations.forEach { mutationItem ->
                val date = reformatDate(mutationItem?.transactionDate!!)

                count+=1
                if (dateFlag != null) {
                    if ((dateFlag.equals(date)) && (count <= allMutations.size)) {
                        temp.add(mutationItem!!)
                    } else {
                        result.add(DailyTransaction(dateFlag, temp.toList()))
//                        monthTransactionDates.add(MonthTransactionDate(dateFlag!!))
                        dateFlag = date
                        temp.clear()
                    }

                    if (count == allMutations.size) {
                        result.add(DailyTransaction(dateFlag, temp.toList()))
                    }
                } else {
                    temp.clear()
                }
            }
            return result
        } else {
            return null
        }
    }

    private fun reformatDate(date: String) : String {
        val sourceFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val converted = sourceFormatter.parse(date)
        val resultFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        return resultFormatter.format(converted)
    }
}