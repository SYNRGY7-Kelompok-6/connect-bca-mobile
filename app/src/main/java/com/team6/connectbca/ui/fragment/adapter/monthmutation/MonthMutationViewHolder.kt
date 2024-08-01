package com.team6.connectbca.ui.fragment.adapter.monthmutation

import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.MonthMutationItemCardBinding
import com.team6.connectbca.databinding.MonthMutationItemHeaderBinding
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MonthTransactionDate
import com.team6.connectbca.domain.model.MutationsItem

class MonthMutationViewHolder(
    private val monthMutationAdapterListener: MonthMutationAdapterListener,
    private val binding: MutationItemRowBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: MutationsItem) {
        binding.ivMutationLogo.setImageResource(R.drawable.ic_logo)
        binding.tvTitle.text = data.type
        binding.tvDesc.text = data.remark
        binding.tvBeneficiaryName.text = data.beneficiaryAccount?.beneficiaryAccountName
        binding.tvBeneficiaryNumber.text = data.beneficiaryAccount?.beneficiaryAccountNumber
        binding.tvPrice.text = data.amount?.value.toString()
    }

    private fun changeStyle(type: String) {
//         TO DO
    }
}

//class MonthMutationHeaderViewHolder(
//    private val binding: MonthMutationItemHeaderBinding
//) : RecyclerView.ViewHolder(binding.root) {
//    fun render(data: MonthTransactionDate) {
//        binding.tvMonth.text = data.dateTime
//    }
//}