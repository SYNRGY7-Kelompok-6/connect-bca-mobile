package com.team6.connectbca.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.TransactionDetail
import com.team6.connectbca.domain.model.TransactionDetailData

@Keep
data class TransactionDetailResponse(

	@field:SerializedName("data")
	val data: TransactionDetailDataResponse? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) {
	fun toEntity() : TransactionDetail {
		return TransactionDetail(
			data = this.data?.toEntity(),
			message = this.message,
			status = this.status
		)
	}
}

@Keep
data class TransactionDetailDataResponse(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("beneficiaryAccountNumber")
	val beneficiaryAccountNumber: String? = null,

	@field:SerializedName("refNumber")
	val refNumber: String? = null,

	@field:SerializedName("beneficiaryName")
	val beneficiaryName: String? = null,

	@field:SerializedName("remark")
	val remark: String? = null,

	@field:SerializedName("sourceName")
	val sourceName: String? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: Long? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("sourceAccountNumber")
	val sourceAccountNumber: String? = null,

	@field:SerializedName("transactionId")
	val transactionId: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null
) {
	fun toEntity() : TransactionDetailData {
		return TransactionDetailData(
			amount = this.amount,
			beneficiaryAccountNumber = this.beneficiaryAccountNumber,
			refNumber = this.refNumber,
			beneficiaryName = this.beneficiaryName,
			remark = this.remark,
			sourceName = this.sourceName,
			transactionDate = this.transactionDate,
			type = this.type,
			sourceAccountNumber = this.sourceAccountNumber,
			transactionId = this.transactionId,
			desc = this.desc
		)
	}
}
