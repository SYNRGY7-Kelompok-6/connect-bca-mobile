package com.team6.connectbca.ui.fragment

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.databinding.FragmentProfileBinding
import com.team6.connectbca.extensions.checkPermissionLogic
import com.team6.connectbca.ui.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModel<ProfileViewModel>()
    private lateinit var imageUri: Uri
    private lateinit var imageCurrentPath: String
    private var imageFile: File? = null
    private var permissionCheckLogic: Boolean = false
    private val REQUEST_CODE_IMAGE_PICKER = 100
    private val REQUEST_CODE_GALLERY = 200

    private val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentProfileBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        setupEditButtonsListener()
        setupEditTextOnFocusChangedListener()
        androidBackButton()


        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.profileProgressBar.visibility = View.VISIBLE
            } else {
                binding.profileProgressBar.visibility = View.GONE
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat data profile", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    imageUri = data.data!!
                }
            }

            imageFile = convertToFile()
        } else {
            Log.e("ERROR GETTING FILE", "hadeh")
        }

        convertToFile()
    }

    private fun setData() {
        viewModel.getUserProfile().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.avatar.load(user.imageUrl)
                binding.etName.setText(user.name)
                binding.etEmail.setText(user.email)
                binding.etPhone.setText(user.phone)
                binding.etBirthDate.setText(user.birth)
                binding.etAddress.setText(user.address)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEditButtonsListener() {
        binding.btnNameEdit.setOnClickListener {
            binding.etName.focusable = View.FOCUSABLE
            binding.etName.isFocusableInTouchMode = true
        }
        binding.btnEmailEdit.setOnClickListener {
            binding.etEmail.focusable = View.FOCUSABLE
            binding.etEmail.isFocusableInTouchMode = true
        }
        binding.btnPhoneEdit.setOnClickListener {
            binding.etPhone.focusable = View.FOCUSABLE
            binding.etPhone.isFocusableInTouchMode = true
        }
        binding.btnBirthDateEdit.setOnClickListener {
            binding.etBirthDate.focusable = View.FOCUSABLE
            binding.etBirthDate.isFocusableInTouchMode = true
        }
        binding.btnAddressEdit.setOnClickListener {
            binding.etAddress.focusable = View.FOCUSABLE
            binding.etAddress.isFocusableInTouchMode = true
        }
        binding.btnChangeAvatar.setOnClickListener {
            permissionCheckLogic = checkPermissionLogic(requireContext())

            if (permissionCheckLogic) {
                showImageDialog()
            } else {
                askForPermission()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEditTextOnFocusChangedListener() {
        binding.etName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etName.focusable = View.NOT_FOCUSABLE
                binding.etName.isFocusableInTouchMode = false
                updateData(name = binding.etName.text.toString())
            }
        }
        binding.etEmail.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etEmail.focusable = View.NOT_FOCUSABLE
                binding.etEmail.isFocusableInTouchMode = false
                updateData(email = binding.etEmail.text.toString())
            }
        }
        binding.etPhone.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etPhone.focusable = View.NOT_FOCUSABLE
                binding.etPhone.isFocusableInTouchMode = false
                updateData(phone = binding.etPhone.text.toString())
            }
        }
        binding.etBirthDate.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etBirthDate.focusable = View.NOT_FOCUSABLE
                binding.etBirthDate.isFocusableInTouchMode = false
                updateData(birth = binding.etBirthDate.text.toString())
            }
        }
        binding.etAddress.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etAddress.focusable = View.NOT_FOCUSABLE
                binding.etAddress.isFocusableInTouchMode = false

                updateData(image = imageFile)
            }
        }
    }

    private fun showImageDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Choose One")
            .setPositiveButton("Galeri") { _,_ -> openGallery() }
            .setNegativeButton("Kamera") { _,_ -> openCamera() }
            .show()
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    CAMERA,
                    READ_MEDIA_IMAGES
                ),
                REQUEST_CODE_IMAGE_PICKER
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    CAMERA,
                    READ_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_IMAGE_PICKER
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    CAMERA,
                    WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_IMAGE_PICKER
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            var photoFile: File? = null

            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("CREATE IMAGE FILE ERROR", e.toString())
            }

            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.team6.connectbca.provider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
            }
        }
    }

    private fun createImageFile() : File {
        val image = File.createTempFile("profile_pict", ".jpg", storageDir)

        imageCurrentPath = image.absolutePath

        return image
    }

    private fun convertToFile() : File {
        val file = File(storageDir, "userProfile")

        val outputStream = FileOutputStream(file)
        val inputStream = requireContext().getContentResolver().openInputStream(imageUri)

        if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        }
        outputStream.flush();

        return file
    }

    private fun updateData(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        birth: String? = null,
        address: String? = null,
        image: File? = null
    ) {
        viewModel.updateUserProfile(
            name,
            email,
            phone,
            birth,
            address,
            image
        ).observe(viewLifecycleOwner) { updatedUser ->
            if (updatedUser != null) {
                if (!name.isNullOrEmpty()) {
                    binding.etName.setText(updatedUser.name)
                } else if (!email.isNullOrEmpty()) {
                    binding.etEmail.setText(updatedUser.email)
                } else if (!phone.isNullOrEmpty()) {
                    binding.etPhone.setText(updatedUser.phone)
                } else if (!birth.isNullOrEmpty()) {
                    binding.etBirthDate.setText(updatedUser.birth)
                } else if (!address.isNullOrEmpty()) {
                    binding.etAddress.setText(updatedUser.address)
                } else {
                    binding.avatar.load(updatedUser.imageUrl)
                }
            }
        }
    }
    private fun androidBackButton(){
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Exit the application
                    requireActivity().finishAffinity()
                }
            }
        )
    }
}