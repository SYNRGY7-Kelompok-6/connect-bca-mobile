package com.team6.connectbca.ui.fragment.adapter.monthmutation

import androidx.recyclerview.widget.DiffUtil
import com.team6.connectbca.domain.model.MutationsItem

class MonthMutationDiffUtil : DiffUtil.ItemCallback<MutationsItem>() {
    override fun areItemsTheSame(oldItem: MutationsItem, newItem: MutationsItem): Boolean {
        return oldItem.remark == newItem.remark
    }

    override fun areContentsTheSame(
        oldItem: MutationsItem,
        newItem: MutationsItem
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}