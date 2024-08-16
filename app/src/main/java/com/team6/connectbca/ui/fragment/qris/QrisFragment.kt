package com.team6.connectbca.ui.fragment.qris

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentQrisBinding
import com.team6.connectbca.databinding.LayoutCustomDialogBinding
import com.team6.connectbca.ui.viewmodel.QrisViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrisFragment : Fragment() {

    private lateinit var binding: FragmentQrisBinding
    private lateinit var codeScanner: CodeScanner
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity()
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Scan QR"
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
        codeScanner = CodeScanner(activity, binding.scannerView)

        codeScanner.decodeCallback = DecodeCallback {
            activity.runOnUiThread {
                handler.removeCallbacks(runnable)
                viewModel.verifyQr(it.text)
                viewModel.getSuccess().observe(viewLifecycleOwner) { isSuccess ->
                    if (isSuccess) {
                        val action =
                            com.team6.connectbca.ui.fragment.QrisFragmentDirections.actionQrisFragmentToQrisPaymentFragment()
                        findNavController().navigate(action)
                    }
                }
                viewModel.getError().observe(viewLifecycleOwner) { error ->
                    if (error != null) {
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
            codeScanner.isFlashEnabled = !codeScanner.isFlashEnabled
            if (codeScanner.isFlashEnabled) {
                DrawableCompat.setTint(
                    binding.ivFlash.drawable,
                    ContextCompat.getColor(activity, R.color.colorPrimary)
                )
                binding.ivFlash.contentDescription =
                    getString(R.string.flash_off_button_description)
                val drawable = binding.ivFlash.background
                DrawableCompat.setTint(
                    drawable,
                    ContextCompat.getColor(activity, android.R.color.white)
                )
            } else {
                DrawableCompat.setTint(
                    binding.ivFlash.drawable,
                    ContextCompat.getColor(activity, android.R.color.white)
                )
                val drawable = binding.ivFlash.background
                binding.ivFlash.contentDescription = getString(R.string.flash_on_button_description)
                DrawableCompat.setTint(
                    drawable,
                    ContextCompat.getColor(activity, R.color.colorPrimary)
                )
            }
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
            binding.cardShowQr.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    android.R.color.white
                )
            )
            binding.tvShowQr.setTextColor(ContextCompat.getColor(activity, android.R.color.black))
            binding.cardScanQr.setCardBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    R.color.colorPrimary
                )
            )
            binding.tvScanQr.setTextColor(ContextCompat.getColor(activity, android.R.color.white))
            handler.removeCallbacks(runnable)
            codeScanner.stopPreview()
            binding.scannerView.visibility = View.GONE
            binding.cardFlash.visibility = View.GONE
            binding.cardGallery.visibility = View.GONE
            binding.cardQrisLogo.visibility = View.GONE
        }

        checkCameraPermission()
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
            startCountdown()
            codeScanner.startPreview()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data

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
}