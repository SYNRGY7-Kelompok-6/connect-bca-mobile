package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.AccountMonthly
import com.team6.connectbca.domain.model.AccountMonthlyData
import com.team6.connectbca.domain.model.MonthlyIncome
import com.team6.connectbca.domain.model.MonthlyOutcome

@Keep
data class AccountMonthlyResponse(

	@field:SerializedName("data")
	val data: AccountMonthlyDataResponse? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) {
	fun toEntity() : AccountMonthly {
		return AccountMonthly(
			data = this.data?.toEntity(),
			message = this.message,
			status = this.status
		)
	}
}

@Keep
data class MonthlyIncomeResponse(

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : MonthlyIncome {
		return MonthlyIncome(
			currency = this.currency,
			value = this.value
		)
	}
}

@Keep
data class AccountMonthlyDataResponse(

	@field:SerializedName("monthlyOutcome")
	val monthlyOutcome: MonthlyOutcomeResponse? = null,

	@field:SerializedName("monthlyIncome")
	val monthlyIncome: MonthlyIncomeResponse? = null
) {
	fun toEntity() : AccountMonthlyData {
		return AccountMonthlyData(
			monthlyOutcome = this.monthlyOutcome?.toEntity(),
			monthlyIncome = this.monthlyIncome?.toEntity()
		)
	}
}

@Keep
data class MonthlyOutcomeResponse(

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
) {
	fun toEntity() : MonthlyOutcome {
		return MonthlyOutcome(
			currency = this.currency,
			value = this.value
		)
	}
}
