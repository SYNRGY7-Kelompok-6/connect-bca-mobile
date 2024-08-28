package com.team6.connectbca.ui.fragment.qris

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentShowQrBinding
import com.team6.connectbca.ui.fragment.adapter.transfer.LatestTransactionAdapter
import com.team6.connectbca.ui.viewmodel.ShowQrViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ShowQrFragment : Fragment(), TextToSpeech.OnInitListener {
    private lateinit var binding: FragmentShowQrBinding
    private lateinit var tts: TextToSpeech
    private var qrImage = ""
    private var expiresAt: Long = 0
    private lateinit var transactionAdapter: LatestTransactionAdapter
    private val viewModel by viewModel<ShowQrViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowQrBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()

        binding.tvExpired.text = getFormattedExpirationTime(expiresAt)

        binding.ivQrCode.setOnClickListener {
            showQrCodeInDialog()
        }

        binding.cardRefresh.setOnClickListener(View.OnClickListener {
            refreshQrCode()
        })
        binding.cardShare.setOnClickListener(View.OnClickListener {
            shareQr()
        })

        initiateToolbar()


        return binding.root
    }

    private fun observeViewModel() {
        viewModel.generateQrCode()
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) {
            if (it != null) {
                val amount = it.balance?.availableBalance?.value
                val name = it.name
                val accountNumber = it.accountNo

                binding.tvName.text = name
                binding.tvAccountNumber.text = accountNumber
                binding.tvBank.text = "Bank Connect"
            }
        }

        viewModel.qrData.observe(viewLifecycleOwner) {
            if (it != null) {
                qrImage = it.qrImage
                expiresAt = it.expiresAt ?: 0
                val formattedExpirationTime = getFormattedExpirationTime(expiresAt)
                binding.tvExpired.text = formattedExpirationTime
                loadImage(qrImage)
            }
        }

        viewModel.getLatestTransaction().observe(viewLifecycleOwner) {
            transactionAdapter.updateData(it?.data ?: emptyList())
        }
    }

    private fun refreshQrCode() {
        viewModel.generateQrCode()
    }

    private fun setupRecyclerView() {
        transactionAdapter = LatestTransactionAdapter(emptyList())
        binding.rvTransfer.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun showQrCodeInDialog() {
        val dialog = Dialog(requireContext())

        // Inflate the custom layout for the dialog
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_qr_large, null)
        val qrImageView = dialogView.findViewById<ImageView>(R.id.ivQrCodeLarge)

        // Load QR code image into the ImageView in dialog
        Glide.with(this)
            .load(qrImage) // Assuming qrImage is a LiveData<String> containing the URL
            .into(qrImageView)

        // Set the content view of the dialog
        dialog.setContentView(dialogView)

        // Remove the default background of the dialog to avoid full-screen mode
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Ensure dialog is not full-screen by setting the width and height
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Enable cancel on touch outside
        dialog.setCanceledOnTouchOutside(true)

        // Show the dialog
        dialog.show()
    }


    private fun loadImage(image: String) {
        Glide.with(this)
            .load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.ivQrCode)
    }

    private fun checkTheme(): Boolean {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(
            android.R.attr.isLightTheme,
            typedValue,
            true
        )
        val isLightTheme = typedValue.data == -1
        var isDarkTheme = !isLightTheme
        return isDarkTheme
    }

    private fun initiateToolbar() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Tampilkan QR"
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
    }


    private fun getDetailAccount() {
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) { balanceInquiry ->
            if (balanceInquiry != null) {
                val amount = balanceInquiry.balance?.availableBalance?.value
                val name = balanceInquiry.name
                val accountNumber = balanceInquiry.accountNo

                binding.tvName.text = name
                binding.tvAccountNumber.text = accountNumber
                binding.tvBank.text = "Bank Connect"

                showSnackbar("Data berhasil dimuat")
            }
        }
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

    private fun getFormattedExpirationTime(expiresAt: Long): String {
        // Ubah expiresAt menjadi objek Date
        val date = Date(expiresAt)

        // Tentukan format yang diinginkan, misalnya "HH:mm"
        val dateFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))

        // Set timezone ke WIB (GMT+7)
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        // Format tanggal menjadi string
        val timeString = dateFormat.format(date)

        // Gabungkan dengan string "Berlaku hingga"
        return "Berlaku hingga $timeString WIB"
    }

    private fun shareQr() {
        Glide.with(this)
            .asBitmap()
            .load(qrImage)
            .into(object : com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?) {
                    // Get the URI for the QR image
                    val imgUri: Uri? = getImageUri(resource)
                    val shareIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, imgUri)
                        type = "image/png"
                    }
                    startActivity(Intent.createChooser(shareIntent, null))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle the case where the image load is cancelled or fails
                }
            })
    }

    private fun getImageUri(bitmap: Bitmap): Uri? {
        var file: File? = null
        var fos: FileOutputStream? = null
        var imageUri: Uri? = null

        try {
            val folder = File("${requireContext().cacheDir}${File.separator}MyTempFiles")
            if (!folder.exists()) {
                folder.mkdir()
            }

            file = File(folder.path, "qr_image.png")
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)

            imageUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return imageUri
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}