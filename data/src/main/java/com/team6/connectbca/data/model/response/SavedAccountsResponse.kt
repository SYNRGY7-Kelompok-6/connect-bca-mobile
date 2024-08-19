package com.team6.connectbca.data.model.response

import com.google.gson.annotations.SerializedName
import com.team6.connectbca.domain.model.SavedAccounts
import com.team6.connectbca.domain.model.SavedAccount

data class SavedAccountsResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<SavedAccountData>
) {
    fun toEntity(): SavedAccounts {
        return SavedAccounts(
            status = this.status,
            message = this.message,
            data = this.data.map { it.toEntity() }
        )
    }
}

data class SavedAccountResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: SavedAccountData
) {
    fun toEntity(): SavedAccount {
        return SavedAccount(
            status = this.status,
            message = this.message,
            data = this.data.toEntity()
        )
    }
}

data class SavedAccountData(
    @SerializedName("savedBeneficiaryId")
    val savedBeneficiaryId: String,

    @SerializedName("beneficiaryAccountNumber")
    val beneficiaryAccountNumber: String,

    @SerializedName("beneficiaryAccountName")
    val beneficiaryAccountName: String,

    @SerializedName("favorite")
    val favorite: Boolean
) {
    fun toEntity(): com.team6.connectbca.domain.model.SavedAccountData {
        return com.team6.connectbca.domain.model.SavedAccountData(
            savedBeneficiaryId = this.savedBeneficiaryId,
            beneficiaryAccountNumber = this.beneficiaryAccountNumber,
            beneficiaryAccountName = this.beneficiaryAccountName,
            favorite = this.favorite
        )
    }
}