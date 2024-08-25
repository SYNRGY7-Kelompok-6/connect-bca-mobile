package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.AccountBalance
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.domain.model.Amount
import com.team6.connectbca.domain.model.AvailableBalance
import com.team6.connectbca.domain.model.Balance
import com.team6.connectbca.domain.model.BankStatement
import com.team6.connectbca.domain.model.BankStatementData
import com.team6.connectbca.domain.model.BeneficiaryAccount
import com.team6.connectbca.domain.model.EndingBalance
import com.team6.connectbca.domain.model.HoldAmount
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.domain.model.SourceAccount
import com.team6.connectbca.domain.model.StartingBalance

@Keep
data class BankStatementResponse(

	@field:SerializedName("data")
	val data: BankStatementDataResponse? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) {
	fun toEntity() : BankStatement {
		return BankStatement (
			data = this.data?.toEntity(),
			message = this.message,
			status = this.status
		)
	}
}

@Keep
data class BankStatementDataResponse(
	@field:SerializedName("accountInfo")
	val accountInfo: AccountInfoResponse? = null,

	@field:SerializedName("mutations")
	val mutations: List<MutationsItemResponse?>? = null,

	@field:SerializedName("accountBalance")
	val accountBalance: AccountBalanceResponse? = null
) {
	fun toEntity() : BankStatementData {
		return BankStatementData (
			accountInfo = this.accountInfo?.toEntity(),
			accountBalance = this.accountBalance?.toEntity(),
			mutations = this.mutations?.map { it?.toEntity() }
		)
	}
}

@Keep
data class SourceAccountResponse(

	@field:SerializedName("sourceAccountName")
	val sourceAccountName: String? = null,

	@field:SerializedName("sourceAccountNumber")
	val sourceAccountNumber: String? = null
) {
	fun toEntity() : SourceAccount {
		return SourceAccount(
			sourceAccountName = this.sourceAccountName,
			sourceAccountNumber = this.sourceAccountNumber
		)
	}
}

@Keep
data class BalanceResponse(

	@field:SerializedName("holdAmount")
	val holdAmount: HoldAmountResponse? = null,

	@field:SerializedName("availableBalance")
	val availableBalance: AvailableBalanceResponse? = null
) {
	fun toEntity() : Balance {
		return Balance(
			holdAmount = this.holdAmount?.toEntity(),
			availableBalance = this.availableBalance?.toEntity()
		)
	}
}

@Keep
data class AccountBalanceResponse(
	@field:SerializedName("endingBalance")
	val endingBalance: EndingBalanceResponse? = null,

	@field:SerializedName("startingBalance")
	val startingBalance: StartingBalanceResponse? = null,
) {
	fun toEntity() : AccountBalance {
		return AccountBalance(
			endingBalance = this.endingBalance?.toEntity(),
			startingBalance = this.startingBalance?.toEntity()
		)
	}
}

@Keep
data class BeneficiaryAccountResponse(

	@field:SerializedName("beneficiaryAccountName")
	val beneficiaryAccountName: String? = null,

	@field:SerializedName("beneficiaryAccountNumber")
	val beneficiaryAccountNumber: String? = null
) {
	fun toEntity() : BeneficiaryAccount {
		return BeneficiaryAccount(
			beneficiaryAccountName = this.beneficiaryAccountName,
			beneficiaryAccountNumber = this.beneficiaryAccountNumber
		)
	}
}

@Keep
data class AmountResponse(

	@field:SerializedName("remainingBalance")
	val remainingBalance: Int? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : Amount {
		return Amount(
			remainingBalance = this.remainingBalance,
			currency = this.currency,
			value = this.value
		)
	}
}

@Keep
data class AvailableBalanceResponse(

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : AvailableBalance {
		return  AvailableBalance(
			currency = this.currency,
			value = this.value
		)
	}
}

@Keep
data class MutationsItemResponse(

	@field:SerializedName("amount")
	val amount: AmountResponse? = null,

	@field:SerializedName("sourceAccount")
	val sourceAccount: SourceAccountResponse? = null,

	@field:SerializedName("beneficiaryAccount")
	val beneficiaryAccount: BeneficiaryAccountResponse? = null,

	@field:SerializedName("remark")
	val remark: String? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: String? = null,

	@field:SerializedName("transactionId")
	val transactionId: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("type")
	val type: String? = null
) {
	fun toEntity() : MutationsItem {
		return MutationsItem(
			amount = this.amount?.toEntity(),
			sourceAccount = this.sourceAccount?.toEntity(),
			beneficiaryAccount = this.beneficiaryAccount?.toEntity(),
			remark = this.remark,
			transactionDate = this.transactionDate,
			transactionId = this.transactionId,
			desc = this.desc,
			type = this.type
		)
	}
}

@Keep
data class HoldAmountResponse(

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : HoldAmount {
		return HoldAmount(
			currency = this.currency,
			value = this.value
		)
	}
}

@Keep
data class EndingBalanceResponse(

	@field:SerializedName("dateTime")
	val dateTime: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : EndingBalance {
		return EndingBalance(
			dateTime = this.dateTime,
			currency = this.currency,
			value = this.value
		)
	}
}

@Keep
data class AccountInfoResponse(

	@field:SerializedName("pinExpiredTimeLeft")
	val pinExpiredTimeLeft: Int? = null,

	@field:SerializedName("accountNo")
	val accountNo: String? = null,

	@field:SerializedName("accountType")
	val accountType: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("balance")
	val balance: BalanceResponse? = null,

	@field:SerializedName("accountCardExp")
	val accountCardExp: String? = null,

	@field:SerializedName("cvv")
	val cvv: String? = null
) {
	fun toEntity() : AccountInfo {
		return AccountInfo(
			pinExpiredTimeLeft = this.pinExpiredTimeLeft,
			accountNo = this.accountNo,
			accountType = this.accountType,
			name = this.name,
			balance = this.balance?.toEntity(),
			accountCardExp = this.accountCardExp,
			cvv = this.cvv
		)
	}
}

@Keep
data class StartingBalanceResponse(

	@field:SerializedName("dateTime")
	val dateTime: String? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : StartingBalance {
		return StartingBalance(
			dateTime = this.dateTime,
			currency = this.currency,
			value = this.value
		)
	}
}
