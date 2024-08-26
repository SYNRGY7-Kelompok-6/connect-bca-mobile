package com.team6.connectbca.ui.fragment.pin

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresExtension
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentPinBinding
import com.team6.connectbca.databinding.LayoutCustomDialogBinding
import com.team6.connectbca.ui.viewmodel.PinViewModel
import com.team6.connectbca.ui.viewmodel.QrisPaymentViewModel
import com.team6.connectbca.ui.viewmodel.TransferViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinFragment : Fragment() {

    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinDigits: MutableList<MaterialTextView>
    private val pinViewModel by viewModel<PinViewModel>()
    private val qrisPaymentViewModel by viewModel<QrisPaymentViewModel>()
    private val transferViewModel by viewModel<TransferViewModel>()

    private var jsonData: JSONObject? = null
    private var transactionType: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            val jsonString = PinFragmentArgs.fromBundle(it).data
            jsonData = JSONObject(jsonString)
            Log.d("PinFragment", "Data: $jsonData")
            Log.d("PinFragment", "Data: ${jsonData?.get("desc") as String}")

            transactionType = PinFragmentArgs.fromBundle(it).transactionType
            Log.d("PinFragment", "Transaction Type: $transactionType")
        }
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinDigits = mutableListOf(
            binding.circle1,
            binding.circle2,
            binding.circle3,
            binding.circle4,
            binding.circle5,
            binding.circle6
        )

        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                pinViewModel.viewModelScope.launch {
                    pinViewModel.addDigit(button.text.toString())
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            pinViewModel.removeDigit()
        }

        pinViewModel.pinLength.observe(viewLifecycleOwner) { length ->
            updatePinCircles(length)
        }

        pinViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.pinProgressBar.visibility = View.VISIBLE
            } else {
                binding.pinProgressBar.visibility = View.GONE
            }
        }

        pinViewModel.pinError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                binding.errorMessage.visibility = View.VISIBLE
            } else {
                binding.errorMessage.visibility = View.GONE
            }
        }

        pinViewModel.pinToken.observe(viewLifecycleOwner) { token ->
            Log.d("PinFragment", "Token: $token")
            if (token != null) {
                Log.d("PinFragment", "Token: $token")
                var amountObject = jsonData?.getJSONObject("amount")
                Log.d("PinFragment", "Amount Object: $amountObject")
                Log.d("PinFragment", "data1")
                val amountValue = when (val value = amountObject?.get("value")) {
                    is Int -> value.toDouble()
                    is Double -> value
                    else -> 0.0
                }
                Log.d("PinFragment", "data2")
                Log.d("PinFragment", "Amount: $amountValue")
                if (transactionType == "qris") {
                    qrisPaymentViewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
                        if (isLoading) {
                            binding.pinProgressBar.visibility = View.VISIBLE
                        } else {
                            binding.pinProgressBar.visibility = View.GONE
                        }
                    }
                    qrisPaymentViewModel.qrisTransfer(
                        token,
                        beneficiaryAccountNumber = jsonData?.get("beneficiaryAccountNumber") as String,
                        remark = jsonData?.get("remark") as String,
                        desc = "Transfer",
                        amountValue = amountValue,
                        currency = amountObject?.get("currency") as String
                    )
                } else if (transactionType == "transfer") {
                    Log.d("PinFragment", jsonData?.get("desc") as String)
                    transferViewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
                        if (isLoading) {
                            binding.pinProgressBar.visibility = View.VISIBLE
                        } else {
                            binding.pinProgressBar.visibility = View.GONE
                        }
                    }
                    transferViewModel.transferInitiate(
                        token,
                        beneficiaryAccountNumber = jsonData?.get("beneficiaryAccountNumber") as String,
                        remark = jsonData?.get("remark") as String,
                        desc = jsonData?.get("desc") as String,
                        amountValue = amountValue,
                        currency = amountObject?.get("currency") as String
                    )
                }
            }
        }

        qrisPaymentViewModel.transferSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Log.d("PinFragment", "Transfer success")
                Log.d(
                    "PinFragment",
                    "Transaction ID: ${qrisPaymentViewModel.transfer.value?.data?.transactionId}"
                )
                successAlertDialog(qrisPaymentViewModel.transfer.value?.data?.transactionId ?: "")
            } else {
                failedAlertDialog()
            }
        }
        transferViewModel.transferSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Log.d("PinFragment", "Transfer success")
                Log.d(
                    "PinFragment",
                    "Transaction ID: ${transferViewModel.transferResult.value?.data?.transactionId}"
                )
                successAlertDialog(
                    transferViewModel.transferResult.value?.data?.transactionId ?: ""
                )
            } else {
                failedAlertDialog()
            }
        }
    }

    private fun updatePinCircles(length: Int) {
        for (i in 0 until 6) {
            if (i < length) {
                pinDigits[i].text = "*"
                pinDigits[i].gravity = View.TEXT_ALIGNMENT_CENTER
                pinDigits[i].setBackgroundResource(R.drawable.filled_circle_background)
            } else {
                pinDigits[i].text = ""
                pinDigits[i].setBackgroundResource(R.drawable.empty_circle_background)
            }
        }
    }

    private fun successAlertDialog(transactionId: String) {
        val dialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = Dialog(requireContext())
        dialogBinding.ivAlert.setBackgroundResource(R.drawable.ic_transaction_success)
        dialogBinding.ivAlert.setImageResource(R.drawable.ic_transaction_success)
        dialogBinding.btnRetry.text = "Lihat Bukti"
        dialogBinding.btnRetry.contentDescription = "Tombol Lihat Bukti"
        dialogBinding.tvTitle.text = "Transaksi Anda Berhasil"
        dialogBinding.tvTitle.marginStart.plus(60)
        dialogBinding.tvTitle.marginEnd.plus(60)

        dialog.dismiss()
        dialogBinding.btnRetry.setOnClickListener {
            dialog.dismiss()
            navigateToPaymentReceipt(transactionId)
        }

        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    private fun failedAlertDialog() {
        val dialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = Dialog(requireContext())
        dialog.dismiss()
        dialogBinding.ivAlert.setBackgroundResource(R.drawable.ic_alert)
        dialogBinding.ivAlert.setImageResource(R.drawable.ic_alert)
        dialogBinding.btnRetry.text = "Ulangi"
        dialogBinding.btnRetry.contentDescription = "Tombol Ulangi"
        dialogBinding.tvTitle.text = "Transaksi Anda Gagal"
        dialogBinding.tvTitle.marginStart.plus(60)
        dialogBinding.tvTitle.marginEnd.plus(60)
        dialogBinding.btnRetry.setOnClickListener {
            dialog.dismiss()
            navigateToHome()
        }

        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    private fun navigateToPaymentReceipt(transactionId: String) {
        val action =
            PinFragmentDirections.actionPinFragmentToPaymentReceiptFragment(transactionId, false)
        findNavController().navigate(action)
    }

    private fun navigateToHome() {
//        navigate to home and clear all backstack and use home as parent
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
