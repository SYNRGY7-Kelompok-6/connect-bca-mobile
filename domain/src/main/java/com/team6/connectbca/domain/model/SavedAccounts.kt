package com.team6.connectbca.domain.model

data class SavedAccounts(
    val status: String? = null,
    val message: String? = null,
    val data: List<SavedAccountData>? = null
)

data class SavedAccount(
    val status: String? = null,
    val message: String? = null,
    val data: SavedAccountData? = null
)

data class SavedAccountData(
    val savedBeneficiaryId: String? = null,
    val beneficiaryAccountNumber: String? = null,
    val beneficiaryAccountName: String? = null,
    val favorite: Boolean? = null
)