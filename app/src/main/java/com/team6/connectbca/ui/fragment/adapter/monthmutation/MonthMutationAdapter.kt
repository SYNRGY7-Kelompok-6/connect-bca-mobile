package com.team6.connectbca.ui.fragment.adapter.monthmutation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MutationsItem

class MonthMutationAdapter (
    private val monthMutationAdapterListener: MonthMutationAdapterListener
) : ListAdapter<MutationsItem, MonthMutationViewHolder>(MonthMutationDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthMutationViewHolder {
        return MonthMutationViewHolder(
            binding = MutationItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            monthMutationAdapterListener = monthMutationAdapterListener
        )
    }

    override fun onBindViewHolder(holder: MonthMutationViewHolder, position: Int) {
        holder.render(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}