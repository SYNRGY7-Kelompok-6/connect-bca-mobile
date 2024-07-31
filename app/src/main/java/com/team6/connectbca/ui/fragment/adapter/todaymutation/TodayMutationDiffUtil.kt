package com.team6.connectbca.ui.fragment.adapter.todaymutation

import androidx.recyclerview.widget.DiffUtil
import com.team6.connectbca.domain.model.MutationsItem

class TodayMutationDiffUtil : DiffUtil.ItemCallback<MutationsItem>() {
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