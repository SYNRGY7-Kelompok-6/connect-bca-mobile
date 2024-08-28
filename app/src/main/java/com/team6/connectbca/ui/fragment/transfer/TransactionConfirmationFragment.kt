package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentTransactionConfirmationBinding
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionConfirmationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentTransactionConfirmationBinding
    private var beneficiaryId: String? = null
    var beneficiaryAccountNumber: String? = null
    var beneficiaryAccountName: String? = null
    private var accountNumber: String? = null
    private var accountBalance: Int = 0
    private var nominalTransactionAmount: Int = 0
    private var destinationBank: String? = null
    private var description: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionConfirmationBinding.inflate(layoutInflater, container, false)
        Log.d("TransactionConfirmationFragment", "${arguments}")
        arguments?.let {
            beneficiaryId = it.getString("beneficiaryId")
            beneficiaryAccountName = it.getString("beneficiaryAccountName")
            beneficiaryAccountNumber = it.getString("beneficiaryAccountNumber")
            destinationBank = it.getString("beneficiaryBank")
            nominalTransactionAmount = it.getInt("nominalTransactionAmount")
            accountNumber = it.getString("accountNumber")
            accountBalance = it.getInt("accountBalance")
            description = it.getString("desc")
        }
        setData()
        initiateToolbar()
        binding.processButton.setOnClickListener(View.OnClickListener {
            val data = mapOf(
                "beneficiaryAccountNumber" to beneficiaryAccountNumber,
                "remark" to "Transfer",
                "desc" to (description?:""),
                "amount" to mapOf(
                    "value" to nominalTransactionAmount,
                    "currency" to "IDR"
                )
            )
            val jsonString = JSONObject(data).toString()
            Log.d("TransactionConfirmationFragment", jsonString)
            navigateToPinFragment(jsonString)
        })
        return binding.root
    }

    private fun setData() {
        binding.logoText.text = getAcronym(beneficiaryAccountName ?: "Pengguna")
        binding.recipientName.text = beneficiaryAccountName
        binding.recipientAccountNumber.text = beneficiaryAccountNumber
        binding.recipientAccountNumber.contentDescription = beneficiaryAccountNumber?.split("")?.joinToString(" ")
        binding.destinationBank.text = destinationBank
        binding.nominalTransactionAmount.text = "Rp ${numberFormat(nominalTransactionAmount)}"
        binding.nominalTransactionAmount.contentDescription =
            "Nominal Transfer $nominalTransactionAmount Rupiah"
        binding.sourceBankId.text = accountNumber
        binding.sourceBankId.contentDescription = accountNumber?.split("")?.joinToString(" ")
        binding.senderBalance.text = "Rp ${numberFormat(accountBalance)}"
        binding.senderBalance.contentDescription = "Saldo Anda $accountBalance Rupiah"
    }

    private fun numberFormat(amount: Int): String {
        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getNumberInstance(localeID)
        numberFormat.minimumFractionDigits = 0
        numberFormat.maximumFractionDigits = 0
        var formatted = numberFormat.format(amount)
        return formatted
    }

    private fun navigateToPinFragment(data: String) {
        val action =
            TransactionConfirmationFragmentDirections.actionTransactionConfirmationFragmentToPinFragment(
                data, "transfer"
            )
        findNavController().navigate(action)
    }

    private fun initiateToolbar() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Cek Detail Penerima"
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
    }

    private fun getAcronym(word: String): String {
        return if (word.length >= 2) {
            val trimmedString = word.substring(2, word.length)
            word.replace(trimmedString, "").uppercase()
        } else {
            word.uppercase()
        }
    }
}