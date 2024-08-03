package com.team6.connectbca.ui.fragment.adapter.searchmutation

import androidx.recyclerview.widget.DiffUtil
import com.team6.connectbca.domain.model.MonthMutationListItem

class SearchMutationDiffUtil : DiffUtil.ItemCallback<MonthMutationListItem>() {
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