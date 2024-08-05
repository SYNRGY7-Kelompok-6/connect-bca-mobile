package com.team6.connectbca.ui.fragment.adapter.searchmutation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.databinding.MonthMutationItemHeaderBinding
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MonthMutationListItem
import com.team6.connectbca.domain.model.MonthTransactionDate
import com.team6.connectbca.domain.model.MutationsItem

class SearchMutationAdapter (
    private val searchMutationAdapterListener: SearchMutationAdapterListener
) : ListAdapter<MonthMutationListItem, RecyclerView.ViewHolder>(SearchMutationDiffUtil()) {
    companion object {
        const val HEADER_VIEW = 0
        const val MUTATION_VIEW = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is MonthTransactionDate -> 0
            is MutationsItem -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HEADER_VIEW -> HeaderViewHolder(
                MonthMutationItemHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            MUTATION_VIEW -> MutationViewHolder(
                searchMutationAdapterListener,
                MutationItemRowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                parent.context
            )
            else -> throw IllegalArgumentException("invalid item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is HeaderViewHolder && item is MonthTransactionDate) {
            holder.render(item)
        } else if (holder is MutationViewHolder && item is MutationsItem) {
            holder.render(item)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty() && payloads.firstOrNull() !is Bundle) {
            onBindViewHolder(holder, position)
        } else {
            // TODO actions based on the payload change
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}