package com.team6.connectbca.ui.fragment.adapter.todaymutation

import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MutationsItem

class TodayMutationViewHolder(
    private val todayMutationAdapterListener: TodayMutationAdapterListener,
    private val binding: MutationItemRowBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: MutationsItem) {
        binding.ivMutationLogo.setImageResource(R.drawable.ic_logo)
        binding.tvTitle.text = data.remark
        binding.tvDesc.text = data.desc
        binding.tvBeneficiaryName.text = data.beneficiaryAccount?.beneficiaryAccountName
        binding.tvBeneficiaryNumber.text = data.beneficiaryAccount?.beneficiaryAccountNumber
        binding.tvPrice.text = data.amount?.value.toString()
        binding.btnNote.setOnClickListener { todayMutationAdapterListener.onClickSeeInvoice() }
    }

    private fun changeStyle(type: String) {
//         TO DO
    }
}