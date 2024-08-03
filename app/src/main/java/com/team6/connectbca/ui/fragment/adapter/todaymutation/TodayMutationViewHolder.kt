package com.team6.connectbca.ui.fragment.adapter.todaymutation

import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ItemMutationRowBinding
import com.team6.connectbca.domain.model.MutationsItem

class TodayMutationViewHolder(
    private val todayMutationAdapterListener: TodayMutationAdapterListener,
    private val binding: ItemMutationRowBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: MutationsItem) {
        binding.ivMutationLogo.setImageResource(R.drawable.ic_logo)
        binding.tvTitle.text = data.type
        binding.tvRemark.text = data.remark
        binding.tvBeneficiaryName.text = data.beneficiaryAccount?.beneficiaryAccountName
        binding.tvBeneficiaryNumber.text = data.beneficiaryAccount?.beneficiaryAccountNumber
        binding.tvPrice.text = data.amount?.value.toString()
    }

    private fun changeStyle(type: String) {
//         TO DO
    }
}