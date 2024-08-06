package com.team6.connectbca.ui.fragment.adapter.monthmutation

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.MonthMutationItemCardBinding
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.extensions.getFormattedBalance

class MutationViewHolder(
    private val monthMutationAdapterListener: MonthMutationAdapterListener,
    private val binding: MutationItemRowBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: MutationsItem) {
        binding.ivMutationLogo.setImageResource(R.drawable.connect_logo)
        binding.tvTitle.text = data.remark
        binding.tvDesc.text = data.desc
        binding.tvBeneficiaryName.text = data.beneficiaryAccount?.beneficiaryAccountName
        binding.tvBeneficiaryNumber.text = data.beneficiaryAccount?.beneficiaryAccountNumber
        binding.tvItemRowCurrency.text = getCurrency(data.amount?.currency!!)
        binding.tvPrice.text = getFormattedBalance(data.amount?.value!!)
        binding.btnNote.setOnClickListener { monthMutationAdapterListener.onClickSeeInvoice() }
        changeStyle(data.type!!)
    }

    private fun changeStyle(type: String) {
        when(type) {
            "CREDIT" -> {
                binding.transactionType.text = "-"
                binding.transactionType.setTextColor(ContextCompat.getColor(context, R.color.secondaryRed))
                binding.tvItemRowCurrency.setTextColor(ContextCompat.getColor(context, R.color.secondaryRed))
                binding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.secondaryRed))
            }
            "DEBIT" -> {
                binding.transactionType.text = "+"
                binding.transactionType.setTextColor(ContextCompat.getColor(context, R.color.secondaryGreen))
                binding.tvItemRowCurrency.setTextColor(ContextCompat.getColor(context, R.color.secondaryGreen))
                binding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.secondaryGreen))
            }
        }
    }

    private fun getCurrency(data: String) : String {
        var result: String = ""

        when(data) {
            "IDR" -> return "Rp"
        }

        return result
    }
}