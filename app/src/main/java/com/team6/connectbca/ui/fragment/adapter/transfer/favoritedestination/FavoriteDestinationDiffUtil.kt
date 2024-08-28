package com.team6.connectbca.ui.fragment.adapter.transfer.favoritedestination

import androidx.recyclerview.widget.DiffUtil
import com.team6.connectbca.domain.model.SavedAccountData

class FavoriteDestinationDiffUtil : DiffUtil.ItemCallback<SavedAccountData>() {
    override fun areItemsTheSame(oldItem: SavedAccountData, newItem: SavedAccountData): Boolean {
        return oldItem.savedBeneficiaryId == newItem.savedBeneficiaryId
    }

    override fun areContentsTheSame(
        oldItem: SavedAccountData,
        newItem: SavedAccountData
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}