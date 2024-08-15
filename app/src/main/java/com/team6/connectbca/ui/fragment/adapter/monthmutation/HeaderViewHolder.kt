package com.team6.connectbca.ui.fragment.adapter.monthmutation

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.MonthMutationItemHeaderBinding
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MonthTransactionDate
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.extensions.getFormattedBalance
import com.team6.connectbca.extensions.reformatDate

class HeaderViewHolder(
    private val binding: MonthMutationItemHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: MonthTransactionDate) {
        val date = reformatDate(data.dateTime, "dd-MM-yyyy", "EEE")

        binding.tvMonthHeader.text = "${changeDayName(date)}, ${data.dateTime.replace("-", " ")}"
        binding.tvMonthHeader.contentDescription = "Daftar transaksi pada hari ${binding.tvMonthHeader.text}"
    }

    private fun changeDayName(date: String) : String {
        when(date) {
            "Sen","Mon" -> return "Senin"
            "Sel","Tue" -> return "Selasa"
            "Rab","Wed" -> return "Rabu"
            "Kam","Thu" -> return "Kamis"
            "Jum","Fri" -> return "Jum'at"
            "Sab","Sat" -> return "Sabtu"
            else -> return "Minggu"
        }
    }
}