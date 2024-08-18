package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.databinding.FragmentPaymentReceiptBinding
import com.team6.connectbca.extensions.getFormattedBalance
import com.team6.connectbca.extensions.milisecondToDateMonth
import com.team6.connectbca.ui.viewmodel.TransferViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentReceiptFragment : Fragment() {
    private var _binding: FragmentPaymentReceiptBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<TransferViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()

        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.paymentReceiptPorgressBar.visibility = View.VISIBLE
            } else {
                binding.paymentReceiptPorgressBar.visibility = View.GONE
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat data transfer", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setData() {
        val transactionId = arguments?.getString("transactionId")

        if (transactionId != null) {
            viewModel.getTransactionDetail(transactionId)
                .observe(viewLifecycleOwner) { transaction ->
                    if (transaction != null) {
                        Log.d("PaymentReceiptFragment", "Transaction: ${transaction.remark?.toLowerCase()}")
                        val date = milisecondToDateMonth(
                            transaction.transactionDate!!,
                            "dd MMM yyyy • HH:mm:ss zzz"
                        )
                        val amount = getFormattedBalance(transaction.amount!!)
                        val sourceNumOld = transaction.sourceAccountNumber!!.substring(0..6)

                        binding.tvTitle.text = transaction.remark
                        binding.tvDate.text = date
                        binding.tvRefNumber.text = "No. Ref: ${transaction.refNumber}"
                        binding.tvRecipientName.text = transaction.beneficiaryName
                        binding.tvTotalTransaction.text = "Rp $amount"
                        binding.tvSourceName.text = transaction.sourceName
                        binding.tvSourceBank.text =
                            transaction.sourceAccountNumber!!.replace(sourceNumOld, "******")
                        binding.tvAcquirer.text = "Bank BCA"
                        binding.btnClose.setOnClickListener {
                            findNavController().popBackStack()
                        }
                        binding.btnShare.setOnClickListener { shareInvoice() }

                        binding.tvTitle.contentDescription = transaction.remark
                        binding.tvDate.contentDescription = date
                        binding.tvRefNumber.contentDescription = "No. Ref: ${transaction.refNumber}"
                        binding.tvRecipientName.contentDescription = transaction.beneficiaryName
                        binding.tvTotalTransaction.contentDescription = "$amount rupiah"
                        binding.tvSourceName.contentDescription = transaction.sourceName
                        binding.tvSourceBank.contentDescription =
                            "Nomor rekening ${binding.tvSourceBank.text}"
                        binding.tvAcquirer.contentDescription = "Bank BCA"
                        binding.btnClose.setOnClickListener { findNavController().popBackStack() }
                        binding.btnShare.setOnClickListener { shareInvoice() }

                        if (!transaction.remark.equals("qris")) {
                            binding.tvQrisRefLabel.visibility = View.GONE
                            binding.tvQrisRef.visibility = View.GONE
                            binding.tvMerchantPanLabel.visibility = View.GONE
                            binding.tvMerchantPan.visibility = View.GONE
                            binding.tvCustomerPanLabel.visibility = View.GONE
                            binding.tvCustomerPan.visibility = View.GONE
                        }

                        Snackbar.make(
                            binding.root,
                            "Bukti transaksi berhasil dimuat",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun shareInvoice() {
        Log.i("Masuk sini", "halo")
    }
}