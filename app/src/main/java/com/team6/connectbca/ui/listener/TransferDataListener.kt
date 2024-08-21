package com.team6.connectbca.ui.listener

import com.team6.connectbca.domain.model.SavedAccountData

interface TransferDataListener {
    fun getBeneficiaryData(): SavedAccountData
}