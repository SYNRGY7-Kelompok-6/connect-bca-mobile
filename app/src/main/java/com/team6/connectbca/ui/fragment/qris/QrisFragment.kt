package com.team6.connectbca.ui.fragment.qris

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentQrisBinding
import com.team6.connectbca.databinding.LayoutCustomDialogBinding
import com.team6.connectbca.ui.viewmodel.QrisViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class QrisFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var binding: FragmentQrisBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var vibrator: Vibrator
    private lateinit var tts: TextToSpeech
    private val viewModel by viewModel<QrisViewModel>()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        showAlertDialog()
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
        private const val REQUEST_CODE_CAMERA_PERMISSION = 123
        private const val TAG = "QrisFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrisBinding.inflate(inflater, container, false)
        viewModel.resetSuccess();
        viewModel.resetError();
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        initiateToolbar()
        codeScanner = CodeScanner(activity, binding.scannerView)

        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                handler.removeCallbacks(runnable)

                viewModel.viewModelScope.launch {
                    if (viewModel.verifyQr(it.text)) {
                        vibrateSuccess()
                        viewModel.qrScanResponse.observe(viewLifecycleOwner) {
                            Log.d(TAG, "QR Scan Response: $it")
                            Log.d(TAG, "QR Scan Response Amount: ${it?.amount}")
                            if (it?.amount?.value != 0.0 && it?.amount?.value != null && it?.amount != null) {
                                val amountMap = mapOf(
                                    "value" to it?.amount?.value,
                                    "currency" to it?.amount?.currency
                                )
                                val data = mapOf(
                                    "beneficiaryName" to it?.beneficiaryName,
                                    "beneficiaryAccountNumber" to it?.beneficiaryAccountNumber,
                                    "remark" to it?.remark,
                                    "amount" to amountMap
                                )
                                val dataString = JSONObject(data).toString()
                                val action = QrisFragmentDirections.actionQrisFragmentToPinFragment(
                                    dataString,
                                    "qris",
                                )
                                findNavController().navigate(action)
                            } else {
                                navigateToQrPayment(viewModel.data.value, true)
                            }
                        }
                    } else {
                        vibrateFailure()
                        showErrorDialog("Kode QR tidak valid")
                    }
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            activity.runOnUiThread {
                Toast.makeText(
                    activity,
                    "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.ivFlash.setOnClickListener(View.OnClickListener {
            buttonFlash()
        })

        binding.cardGallery.setOnClickListener {
            pickImageFromGallery()
        }

        binding.cardScanQr.setOnClickListener {
            binding.cardScanQr.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    android.R.color.white
                )
            )
            binding.tvScanQr.setTextColor(ContextCompat.getColor(activity, android.R.color.black))
            binding.cardShowQr.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.colorPrimary
                )
            )
            binding.tvShowQr.setTextColor(ContextCompat.getColor(activity, android.R.color.white))
            startCountdown()  // Start the countdown when preview starts
            codeScanner.startPreview()
            binding.scannerView.visibility = View.VISIBLE
            binding.cardFlash.visibility = View.VISIBLE
            binding.cardGallery.visibility = View.VISIBLE
            binding.cardQrisLogo.visibility = View.VISIBLE
        }

        binding.cardShowQr.setOnClickListener {
            navigateToShowQrFragment()
        }

        checkCameraPermission()
    }

    private fun buttonFlash() {
        codeScanner.isFlashEnabled = !codeScanner.isFlashEnabled
        if (codeScanner.isFlashEnabled) {
            DrawableCompat.setTint(
                binding.ivFlash.drawable,
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
            binding.ivFlash.contentDescription =
                getString(R.string.flash_off_button_description)
            val drawable = binding.ivFlash.background
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )
        } else {
            DrawableCompat.setTint(
                binding.ivFlash.drawable,
                ContextCompat.getColor(requireContext(), android.R.color.white)
            )
            val drawable = binding.ivFlash.background
            binding.ivFlash.contentDescription = getString(R.string.flash_on_button_description)
            DrawableCompat.setTint(
                drawable,
                ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            )
        }
    }

    private fun initiateToolbar() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Scan QR"
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
    }

    private fun navigateToShowQrFragment() {
        binding.cardShowQr.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )
        binding.tvShowQr.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.black
            )
        )
        binding.cardScanQr.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorPrimary
            )
        )
        binding.tvScanQr.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.white
            )
        )
        handler.removeCallbacks(runnable)
        codeScanner.stopPreview()
        binding.scannerView.visibility = View.GONE
        binding.cardFlash.visibility = View.GONE
        binding.cardGallery.visibility = View.GONE
        binding.cardQrisLogo.visibility = View.GONE
        var data = JSONObject()
        navigateToQrPayment(data, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateSuccess() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateFailure() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    private fun startCountdown() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 30000)
    }

    private fun showAlertDialog() {
        val dialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = Dialog(requireContext())
        dialog.dismiss()
        dialogBinding.btnRetry.setOnClickListener {
            dialog.dismiss()
            startCountdown()
        }

        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    private fun showErrorDialog(errorText: String) {

        val dialogBinding = LayoutCustomDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = Dialog(requireContext())
        dialogBinding.tvTitle.text = errorText
        dialogBinding.btnRetry.setOnClickListener {
            dialog.dismiss()
            handler.postDelayed({
                codeScanner.startPreview()
                startCountdown()
            }, 2000)
        }

        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun navigateToQrPayment(data: JSONObject?, isScan: Boolean) {
        if (data != null) {
            val action = QrisFragmentDirections.actionQrisFragmentToQrisPaymentFragment(
                data.toString(),
                isScan,
            )
            findNavController().navigate(action)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                scanQRFromBitmap(bitmap)
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA_PERMISSION
            )
        } else {
            codeScanner.startPreview()
            startCountdown()  // Start countdown when permission is granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                codeScanner.startPreview()
                startCountdown()  // Start countdown when permission is granted
            } else {
                Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scanQRFromBitmap(bitmap: Bitmap) {
        val intArray = IntArray(bitmap.width * bitmap.height)
        bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        try {
            val result = MultiFormatReader().decode(binaryBitmap)
            val qrContent = result.text
            viewModel.viewModelScope.launch {
                if (viewModel.verifyQr(qrContent)) {
                    vibrateSuccess()
                    navigateToQrPayment(viewModel.data.value, true)
                } else {
                    vibrateFailure()
                    showErrorDialog("Kode QR tidak valid")
                }
            }
        } catch (e: NotFoundException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "QR Code tidak ditemukan", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        startCountdown()  // Restart countdown on resume
    }

    override fun onPause() {
        codeScanner.releaseResources()
        handler.removeCallbacks(runnable)  // Stop the countdown
        super.onPause()
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
}