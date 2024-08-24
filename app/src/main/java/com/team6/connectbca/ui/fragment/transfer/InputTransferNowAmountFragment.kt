package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.team6.connectbca.databinding.FragmentInputTransferNowAmountBinding
import com.team6.connectbca.domain.model.AccountInfo
import com.team6.connectbca.ui.listener.TransferDataListener
import com.team6.connectbca.ui.viewmodel.InputTransferAmountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale

class InputTransferNowAmountFragment : Fragment() {
    private var _binding: FragmentInputTransferNowAmountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<InputTransferAmountViewModel>()
    private var accountNumber: String? = null
    private var accountBalance: Int = 0
    private var beneficiaryIdFromArgument: String? = null
    private var beneficiaryAccountNumberFromArgument: String? = null
    private var beneficiaryAccountNameFromArgument: String? = null
    private var currentText = ""
    private var transferDataListener: TransferDataListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputTransferNowAmountBinding.inflate(layoutInflater, container, false)
        transferDataListener = parentFragment as? TransferDataListener
        val beneficiaryData = transferDataListener?.getBeneficiaryData()
        beneficiaryIdFromArgument = beneficiaryData?.savedBeneficiaryId
        beneficiaryAccountNumberFromArgument = beneficiaryData?.beneficiaryAccountNumber
        beneficiaryAccountNameFromArgument = beneficiaryData?.beneficiaryAccountName
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) {
            var data = it
            accountBalance = data?.balance?.availableBalance?.value ?: 0
            accountNumber = data?.accountNo
            setData(data ?: AccountInfo())
        }

        viewModel.beneficiaryNameFromArgument.observe(viewLifecycleOwner) {
            beneficiaryAccountNameFromArgument = it
            Log.d("InputTransferNowAmountFragment", "Beneficiary ID: $beneficiaryAccountNameFromArgument")
        }

        binding.continueButton.setOnClickListener {
            navigateToConfirmation(
                beneficiaryId = beneficiaryIdFromArgument ?: "",
                beneficiaryBank = "BCA",
                beneficiaryAccountNumber = beneficiaryAccountNumberFromArgument ?: "",
                beneficiaryName = beneficiaryAccountNameFromArgument ?: "",
                accountNumber = accountNumber ?: "",
                accountBalance = accountBalance,
                nominalTransactionAmount = binding.inputNominal.text.toString().replace(".", "")
                    .toIntOrNull() ?: 0,
                description = binding.inputDescription.text.toString().ifEmpty {
                    ""
                }
            )
        }
        setupTextInputWatcher()
        return binding.root
    }

    private fun setupTextInputWatcher() {
        binding.inputNominal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()

                // Prevent '0' as the first character
                if (text.isNotEmpty() && text.startsWith("0")) {
                    binding.inputNominal.setText(text.substring(1))
                    return
                }

                // Handle formatting for thousand separators
                if (text != currentText) {
                    binding.inputNominal.removeTextChangedListener(this)

                    val cleanText = text.replace(".", "")
                    val formattedText = cleanText.toLongOrNull()?.let {
                        String.format("%,d", it).replace(",", ".")
                    }

                    currentText = formattedText ?: ""
                    binding.inputNominal.setText(currentText)
                    binding.inputNominal.setSelection(currentText.length)

                    binding.inputNominal.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setData(accountInfo: AccountInfo) {
        if (accountInfo != null) {
            val amount = accountInfo.balance?.availableBalance?.value
            val accountNumber = accountInfo.accountNo
            val numberFormat = NumberFormat.getNumberInstance(Locale("id", "ID"))
            numberFormat.minimumFractionDigits = 0
            numberFormat.maximumFractionDigits = 0
            val formattedAmount = numberFormat.format(amount)
            binding.sourceBankId.text = accountNumber
            binding.senderBalance.text = "Rp $formattedAmount"
            binding.senderBalance.contentDescription = "$amount Rupiah"

        }
    }

    private fun navigateToConfirmation(
        beneficiaryId: String,
        beneficiaryBank: String,
        beneficiaryAccountNumber: String,
        beneficiaryName: String,
        accountNumber: String,
        accountBalance: Int,
        nominalTransactionAmount: Int,
        description: String,
    ) {
        val action =
            InputTransferAmountFragmentDirections.actionInputTransferAmountFragmentToTransactionConfirmationFragment(
                beneficiaryId,
                beneficiaryAccountNumber,
                beneficiaryName,
                nominalTransactionAmount,
                accountNumber,
                accountBalance,
                beneficiaryBank,
                description

            )
        findNavController().navigate(action)
    }
}