package com.team6.connectbca.ui.fragment.adapter.todaymutation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MutationsItem

class TodayMutationAdapter (
    private val todayMutationAdapterListener: TodayMutationAdapterListener
) : ListAdapter<MutationsItem, TodayMutationViewHolder>(TodayMutationDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMutationViewHolder {
        return TodayMutationViewHolder(
            binding = MutationItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            todayMutationAdapterListener = todayMutationAdapterListener,
            context = parent.context
        )
    }

    override fun onBindViewHolder(holder: TodayMutationViewHolder, position: Int) {
        holder.render(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

}