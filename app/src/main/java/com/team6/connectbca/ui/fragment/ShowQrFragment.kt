package com.team6.connectbca.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentShowQrBinding
import com.team6.connectbca.domain.model.ShowQr
import com.team6.connectbca.domain.model.ShowQrData
import com.team6.connectbca.ui.viewmodel.ShowQrViewModel
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ShowQrFragment : Fragment(), TextToSpeech.OnInitListener {
    private lateinit var binding: FragmentShowQrBinding
    private lateinit var tts: TextToSpeech
    private var args: JSONObject? = null
    private var qrImage = ""
    private var expiresAt: Long = 0
    private var amount: Double = 0.0
    private var currency: String = ""
    private val viewModel by viewModel<ShowQrViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowQrBinding.inflate(inflater, container, false)
        arguments?.let {
            qrImage = arguments?.getString("qrImage") ?: ""
            expiresAt = arguments?.getLong("expiresAt") ?: 0
            amount = arguments?.getFloat("amount")?.toDouble() ?: 0.0
            currency = arguments?.getString("currency") ?: ""
        }
        val formattedExpirationTime = getFormattedExpirationTime(expiresAt)
        binding.tvExpired.text = formattedExpirationTime
        loadImage(qrImage)
        getDetailAccount()
        viewModel.qrImage.observe(viewLifecycleOwner) {
            loadImage(it)
        }
        binding.ivQrCode.setOnClickListener {
            showQrCodeInDialog()
        }

        binding.cardRefresh.setOnClickListener(View.OnClickListener {
            refreshQrCode()
        })

        viewModel.qrData.observe(viewLifecycleOwner) {
            if (it != null) {
                qrImage = it.qrImage
                expiresAt = it.expiresAt ?: 0
                val formattedExpirationTime = getFormattedExpirationTime(expiresAt)
                binding.tvExpired.text = formattedExpirationTime
                loadImage(qrImage)
            }
        }

        initiateToolbar()

        backAndroidButton()

        return binding.root
    }

    private fun refreshQrCode() {
        viewModel.showQrTransfer(amount, currency)
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
        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
            findNavController().popBackStack()
            findNavController().popBackStack()
        })
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

                Snackbar.make(binding.root, "Data berhasil dimuat", Snackbar.LENGTH_LONG).show()
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

    private fun backAndroidButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }
            })
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



}