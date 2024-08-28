package com.team6.connectbca.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ItemTransactionBinding
import com.team6.connectbca.domain.model.SavedAccountData
import com.team6.connectbca.domain.model.TransactionData
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LatestTransactionAdapter(private var items: List<TransactionData>) :
    RecyclerView.Adapter<LatestTransactionAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TransactionData) {
            binding.apply {
                sourceName.text = data.sourceName
                amount.text = "+ Rp ${numberFormat(data.amount ?: 0.0)}"
                amount.contentDescription = "Saldo Masuk ${numberFormat(data.amount ?: 0.0)} Rupiah"
                transactionDate.text = formatDate(data.transactionDate ?: "")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return minOf(items.size, 2)
    }

    fun updateData(newTransactions: List<TransactionData>) {
        items = newTransactions
        notifyDataSetChanged()
    }

    private fun numberFormat(amount: Double): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getNumberInstance(localeID)
        numberFormat.minimumFractionDigits = 0
        numberFormat.maximumFractionDigits = 0
        var formatted = numberFormat.format(amount)
        return formatted
    }

    private fun formatDate(date: String): String? {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
        val parsedDate: Date? = inputFormat.parse(date)
        return parsedDate?.let { outputFormat.format(it) }
    }
}