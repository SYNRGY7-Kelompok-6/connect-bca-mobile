package com.team6.connectbca.ui.fragment.qris

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentQrisPaymentBinding
import com.team6.connectbca.extensions.getFormattedBalance
import com.team6.connectbca.ui.viewmodel.QrisPaymentViewModel
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale


class QrisPaymentFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentQrisPaymentBinding
    private lateinit var tts: TextToSpeech
    private val viewModel by viewModel<QrisPaymentViewModel>()

    private var jsonData: JSONObject? = null
    private var isScan: Boolean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val jsonString = QrisPaymentFragmentArgs.fromBundle(it).data
            isScan = QrisPaymentFragmentArgs.fromBundle(it).isScan
            Log.d("QrisPaymentFragment", jsonString)
            jsonData = JSONObject(jsonString)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrisPaymentBinding.inflate(inflater, container, false)
        navigateBack()
        getDetailAccount()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("QrisPaymentFragment", jsonData.toString())
        if (isScan == true) {
            binding.beneficiaryAccountName.text = jsonData?.get("beneficiaryName") as String
            var accountNumber = jsonData?.get("beneficiaryAccountNumber") as String
            binding.beneficiaryAccountNumber.text = accountNumber
            val spacedAccountNumber = accountNumber?.map { it -> "$it " }?.joinToString(" ")
            binding.beneficiaryAccountNumber.contentDescription = "Nomor rekening tujuan $spacedAccountNumber"
            binding.remark.text = jsonData?.get("remark") as String
        }
        binding.tvBankAccount.text = "Bank Connect"
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.addDigit(button.text.toString())
            }
        }

        binding.tvTotalAmount.text = "Rp 0"
        binding.tvTotalAmount.contentDescription = "0 rupiah"
        binding.btnDelete.setOnClickListener {
            viewModel.removeDigit()
        }
        viewModel.amount.observe(viewLifecycleOwner) {
            val number = it.toDoubleOrNull() ?: 0.0
            val numberFormat = NumberFormat.getNumberInstance(Locale("id", "ID"))
            numberFormat.minimumFractionDigits = 0
            numberFormat.maximumFractionDigits = 0
            val formattedAmount = numberFormat.format(number)
            binding.tvTotalAmount.text = "Rp $formattedAmount"
            binding.tvTotalAmount.contentDescription = "$it rupiah"
        }

        binding.btnDone.setOnClickListener {
            viewModel.amount.value?.let {
                val amount = it.toDouble()
                if (amount > 100) {
                    if (isScan == true){
                        val data = mapOf(
                            "beneficiaryAccountNumber" to (jsonData?.get("beneficiaryAccountNumber") ?: "" ),
                            "remark" to (jsonData?.get("remark") ?: ""),
                            "desc" to "QR Payment",
                            "amount" to mapOf(
                                "value" to amount,
                                "currency" to "IDR"
                            ),
                        )
                        val dataString = JSONObject(data).toString()
                        navigateToPinConfirmation(dataString, "qris")
                    }else{
                        val data = mapOf(
                            "amount" to mapOf(
                                "value" to amount,
                                "currency" to "IDR"
                            ),
                        )
                        val dataString = JSONObject(data).toString()
                        navigateToPinConfirmation(dataString, "show_qris")
                    }
                }else{
                    showSnackbar("Jumlah Transaksi minimal Rp 10.000")
                }
            }
        }
    }
    private fun getDetailAccount(){
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) { balanceInquiry ->
            if (balanceInquiry != null) {
                val amount = balanceInquiry.balance?.availableBalance?.value
                val name = balanceInquiry.name
                val accountNumber = balanceInquiry.accountNo
                val balanceInquiry = getFormattedBalance(balance = amount ?: 0)
                binding.tvAccountNumber.text = accountNumber
                val spacedAccountNumber = accountNumber?.map { it -> "$it " }?.joinToString(" ")
                binding.tvAccountNumber.contentDescription =
                    "Nomor rekening anda $spacedAccountNumber"
                binding.tvBalanceInquiry.text = "Rp $balanceInquiry"
                binding.tvBalanceInquiry.contentDescription = "Saldo anda $balanceInquiry rupiah"

                showSnackbar("Data berhasil dimuat")
            }
        }
    }

    private fun navigateToPinConfirmation(data: String, transactionType: String){
        val action = QrisPaymentFragmentDirections.actionQrisPaymentFragmentToPinFragment(data, transactionType)
        findNavController().navigate(action)
    }
    private fun navigateBack() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = ""
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("id", "ID"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported
                Log.e("TTS", "Indonesian language is not supported")
            }
        } else {
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}