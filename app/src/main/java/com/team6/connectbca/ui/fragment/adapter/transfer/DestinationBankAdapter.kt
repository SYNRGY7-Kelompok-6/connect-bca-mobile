package com.team6.connectbca.ui.fragment.adapter.transfer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.databinding.ItemDestinationBankBinding
import com.team6.connectbca.domain.model.SavedAccountData

class DestinationBankAdapter(
    private var accounts: List<SavedAccountData>,
    private val onItemClick: (SavedAccountData) -> Unit
) : RecyclerView.Adapter<DestinationBankAdapter.ViewHolder>(), Filterable {

    private var filteredAccounts: List<SavedAccountData> = accounts

    inner class ViewHolder(
        private val binding: ItemDestinationBankBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(account: SavedAccountData) {
            binding.tvDestinationBankName.text = account.beneficiaryAccountName
            itemView.setOnClickListener {
                onItemClick(account)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDestinationBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredAccounts[position])
    }

    override fun getItemCount(): Int = filteredAccounts.size

    fun updateData(newAccounts: List<SavedAccountData>) {
        accounts = newAccounts
        filteredAccounts = newAccounts
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""

                filteredAccounts = if (query.isEmpty()) {
                    accounts
                } else {
                    accounts.filter {
                        it.beneficiaryAccountName?.lowercase()?.contains(query) == true
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = filteredAccounts
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredAccounts = results?.values as List<SavedAccountData>
                notifyDataSetChanged()
            }
        }
    }
}