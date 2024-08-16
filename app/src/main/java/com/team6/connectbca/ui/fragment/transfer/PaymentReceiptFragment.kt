package com.team6.connectbca.ui.fragment.transfer

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.databinding.FragmentPaymentReceiptBinding
import com.team6.connectbca.extensions.getFormattedBalance
import com.team6.connectbca.extensions.milisecondToDateMonth
import com.team6.connectbca.ui.viewmodel.TransferViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
                Snackbar.make(binding.root, "Gagal memuat data transfer", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setData() {
        val transactionId = arguments?.getString("transactionId")

        if (transactionId != null) {
            viewModel.getTransactionDetail(transactionId).observe(viewLifecycleOwner) {transaction ->
                if (transaction != null) {
                    val date = milisecondToDateMonth(transaction.transactionDate!!, "dd MMM yyyy • HH:mm:ss zzz")
                    val amount = getFormattedBalance(transaction.amount!!)
                    val sourceNumOld = transaction.sourceAccountNumber!!.substring(0..6)

                    binding.tvTitle.text = transaction.remark
                    binding.tvDate.text = date
                    binding.tvRefNumber.text = "No. Ref: ${transaction.refNumber}"
                    binding.tvRecipientName.text = transaction.beneficiaryName
                    binding.tvTotalTransaction.text = "Rp $amount"
                    binding.tvSourceName.text = transaction.sourceName
                    binding.tvSourceBank.text = transaction.sourceAccountNumber!!.replace(sourceNumOld, "******")
                    binding.tvAcquirer.text = "Bank BCA"
                    binding.btnClose.setOnClickListener { parentFragmentManager.popBackStack() }
                    binding.btnShare.setOnClickListener { shareInvoice() }

                    binding.tvTitle.contentDescription = transaction.remark
                    binding.tvDate.contentDescription = date
                    binding.tvRefNumber.contentDescription = "No. Ref: ${transaction.refNumber}"
                    binding.tvRecipientName.contentDescription = transaction.beneficiaryName
                    binding.tvTotalTransaction.contentDescription = "$amount rupiah"
                    binding.tvSourceName.contentDescription = transaction.sourceName
                    binding.tvSourceBank.contentDescription = "Nomor rekening ${binding.tvSourceBank.text}"
                    binding.tvAcquirer.contentDescription = "Bank BCA"

                    if (!transaction.remark.equals("qris")) {
                        binding.tvQrisRefLabel.visibility = View.GONE
                        binding.tvQrisRef.visibility = View.GONE
                        binding.tvMerchantPanLabel.visibility = View.GONE
                        binding.tvMerchantPan.visibility = View.GONE
                        binding.tvCustomerPanLabel.visibility = View.GONE
                        binding.tvCustomerPan.visibility = View.GONE
                    }

                    Snackbar.make(binding.root, "Bukti transaksi berhasil dimuat", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun shareInvoice() {
        val viewBitmap = convertLayoutToImage()
        val imgUri: Uri? = getImageUri(viewBitmap)
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imgUri)
            type = "image/png"
        }
        startActivity(Intent.createChooser(shareIntent, null))
    }

    private fun convertLayoutToImage() : Bitmap {
        hideButtons()

        val bitmap: Bitmap = Bitmap.createBitmap(binding.root.width, binding.root.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)

        binding.root.draw(canvas)
        showButtons()

        return bitmap
    }

    private fun getImageUri(bitmap: Bitmap) : Uri? {
        var file: File? = null
        var fos1: FileOutputStream? = null
        var imageUri: Uri? = null

        try {
            val folder: File =
                File("${requireContext().cacheDir}${File.separator}MyTempFiles")
            var success = true
            val filename = "img.jpg"

            if (!folder.exists()) {
                success = folder.mkdir()
            }

            file = File(folder.path, filename)
            fos1 = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos1)

            imageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
        } catch (_: Exception) {
        } finally {
            try {
                fos1!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return imageUri
    }

    private fun hideButtons() {
        binding.btnShare.visibility = View.GONE
        binding.btnClose.visibility = View.GONE
    }

    private fun showButtons() {
        binding.btnShare.visibility = View.VISIBLE
        binding.btnClose.visibility = View.VISIBLE
    }
}