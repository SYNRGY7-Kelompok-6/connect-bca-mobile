package com.team6.connectbca.ui.fragment.adapter.monthmutation

import androidx.recyclerview.widget.DiffUtil
import com.team6.connectbca.domain.model.MonthMutationListItem
import com.team6.connectbca.domain.model.MutationsItem

class MonthMutationDiffUtil : DiffUtil.ItemCallback<MonthMutationListItem>() {
    override fun areItemsTheSame(oldItem: MonthMutationListItem, newItem: MonthMutationListItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: MonthMutationListItem,
        newItem: MonthMutationListItem
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}