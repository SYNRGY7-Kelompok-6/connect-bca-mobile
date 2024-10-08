package com.team6.connectbca.ui.fragment.adapter.searchmutation

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.MutationItemRowBinding
import com.team6.connectbca.domain.model.MutationsItem
import com.team6.connectbca.extensions.getFormattedBalance

class MutationViewHolder(
    private val searchMutationAdapterListener: SearchMutationAdapterListener,
    private val binding: MutationItemRowBinding,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {
    fun render(data: MutationsItem) {
        binding.tvTitle.text = data.remark
        binding.tvTitle.contentDescription = binding.tvTitle.text
        binding.tvDesc.text = data.desc
        binding.tvDesc.contentDescription = binding.tvDesc.text
        binding.tvBeneficiaryName.text = data.beneficiaryAccount?.beneficiaryAccountName
        binding.tvBeneficiaryNumber.text = data.beneficiaryAccount?.beneficiaryAccountNumber
        binding.tvBeneficiaryName.contentDescription = "Nama akun ${binding.tvBeneficiaryName.text}"
        binding.tvBeneficiaryNumber.contentDescription = "Nomor akun ${binding.tvBeneficiaryNumber.text.split("").joinToString(" ")}"
        binding.tvItemRowCurrency.text = getCurrency(data.amount?.currency!!)
        binding.tvPrice.text = getFormattedBalance(data.amount?.value!!)
        binding.tvPrice.contentDescription = "Jumlah nominal ${data.amount?.value!!}"
        binding.btnNote.setOnClickListener { searchMutationAdapterListener.onClickSeeInvoice(data.transactionId!!) }
        binding.btnNote.contentDescription = "Tombol melihat bukti transaksi"
        changeStyle(data.type!!)
        setLogo()
    }

    private fun changeStyle(type: String) {
        when(type) {
            "DEBIT" -> {
                binding.transactionType.text = "-"
                binding.transactionType.contentDescription = "Dana keluar"
                binding.transactionType.setTextColor(ContextCompat.getColor(context, R.color.secondaryRed))
                binding.tvItemRowCurrency.setTextColor(ContextCompat.getColor(context, R.color.secondaryRed))
                binding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.secondaryRed))
            }
            "CREDIT" -> {
                binding.transactionType.text = "+"
                binding.transactionType.contentDescription = "Dana masuk"
                binding.transactionType.setTextColor(ContextCompat.getColor(context, R.color.secondaryGreen))
                binding.tvItemRowCurrency.setTextColor(ContextCompat.getColor(context, R.color.secondaryGreen))
                binding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.secondaryGreen))
            }
        }
    }

    private fun setLogo() {
        if (binding.tvTitle.text.equals("Transfer")) {
            binding.ivMutationLogo.setImageResource(R.drawable.ic_transfer)
        } else {
            binding.ivMutationLogo.setImageResource(R.drawable.ic_qris)
        }
    }

    private fun getCurrency(data: String) : String {
        var result: String = ""

        when(data) {
            "IDR" -> {
                binding.tvItemRowCurrency.contentDescription = "Rupiah"
                return "Rp"
            }
        }

        return result
    }
}