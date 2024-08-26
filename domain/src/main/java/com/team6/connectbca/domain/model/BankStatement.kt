package com.team6.connectbca.domain.model

data class BankStatement(
    val data: BankStatementData? = null,
    val message: String? = null,
    val status: String? = null
)

data class BankStatementData(
    val accountInfo: AccountInfo? = null,
    val mutations: List<MutationsItem?>? = null,
    val accountBalance: AccountBalance? = null
)

data class SourceAccount(
    val sourceAccountName: String? = null,
    val sourceAccountNumber: String? = null
)

data class Balance(
    val holdAmount: HoldAmount? = null,
    val availableBalance: AvailableBalance? = null
)

data class AccountBalance(
    val endingBalance: EndingBalance? = null,
    val startingBalance: StartingBalance? = null,
)

data class BeneficiaryAccount(
    val beneficiaryAccountName: String? = null,
    val beneficiaryAccountNumber: String? = null
)

data class Amount(
    val remainingBalance: Int? = null,
    val currency: String? = null,
    val value: Int? = null
)

data class AvailableBalance(
    val currency: String? = null,
    val value: Int? = null
)

data class MutationsItem(
    val amount: Amount? = null,
    val sourceAccount: SourceAccount? = null,
    val beneficiaryAccount: BeneficiaryAccount? = null,
    val remark: String? = null,
    val transactionDate: String? = null,
    val transactionId: String? = null,
    val desc: String? = null,
    val type: String? = null
) : MonthMutationListItem()

data class HoldAmount(
    val currency: String? = null,
    val value: Int? = null
)

data class EndingBalance(
    val dateTime: String? = null,
    val currency: String? = null,
    val value: Int? = null
)

data class AccountInfo(
    val pinExpiredTimeLeft: Int? = null,
    val accountNo: String? = null,
    val accountType: String? = null,
    val name: String? = null,
    val balance: Balance? = null,
    val accountCardExp: String? = null,
    val cvv: String? = null
)

data class StartingBalance(
    val dateTime: String? = null,
    val currency: String? = null,
    val value: Int? = null
)

data class DailyTransaction(
    val dateTime: MonthTransactionDate?,
    val transactionGroup: List<MutationsItem>?
)

data class MonthTransactionDate(
    val dateTime: String
) : MonthMutationListItem()
