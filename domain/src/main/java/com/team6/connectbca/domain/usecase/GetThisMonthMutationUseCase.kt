package com.team6.connectbca.domain.usecase

import com.team6.connectbca.domain.model.DailyTransaction
import com.team6.connectbca.domain.model.MonthTransactionDate
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.repository.BankStatementRepository

class GetThisMonthMutationUseCase(
    private val bankStatementRepository: BankStatementRepository
) {
    suspend operator fun invoke(
        token: String,
        fromDate: String,
        toDate: String,
        page: String,
        pageSize: String
    ): List<DailyTransaction>? {
        val allMutations = bankStatementRepository.getBankStatement(token, fromDate, toDate, page, pageSize)?.mutations
        val result : MutableList<DailyTransaction> = mutableListOf()
        var temp: MutableList<MutationsItem> = mutableListOf()
//        var monthTransactionDates: MutableList<MonthTransactionDate> = mutableListOf()
        var dateFlag = allMutations?.firstOrNull()?.transactionDate

        if (allMutations != null) {
            allMutations.forEach {mutationItem ->
                val date = mutationItem?.transactionDate

                if (dateFlag != null) {
                    if (dateFlag.equals(date)) {
                        temp.add(mutationItem!!)
                    } else {
                        result.add(DailyTransaction(dateFlag, temp))
//                        monthTransactionDates.add(MonthTransactionDate(dateFlag!!))
                        dateFlag = date
                        temp.clear()
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
}