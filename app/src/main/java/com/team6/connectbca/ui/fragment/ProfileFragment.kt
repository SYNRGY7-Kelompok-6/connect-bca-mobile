package com.team6.connectbca.ui.fragment

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentProfileBinding
import com.team6.connectbca.extensions.checkPermissionLogic
import com.team6.connectbca.extensions.getTimeStamp
import com.team6.connectbca.extensions.getYear
import com.team6.connectbca.ui.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModel<ProfileViewModel>()
    private lateinit var imageUri: Uri
    private var storageDir: File? = null
    private var imageFile: File? = null
    private var permissionCheckLogic: Boolean = false
    private val REQUEST_CODE_PERMISSION = 100
    private val REQUEST_CODE_GALLERY = 200
    private val REQUEST_CODE_CAMERA = 300

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
        storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty()) {
                var flag = true

                grantResults.forEach { result ->
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        flag = false
                    }
                }

                if (flag) {
                    showImageDialog()
                } else {
                    Snackbar.make(binding.root, "Permission is denied", Snackbar.LENGTH_SHORT)
                        .show()
                }
            } else {
                Snackbar.make(binding.root, "Permission is denied", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("MASUK GAK", "MASUK")
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    Log.i("ISI DATA", data.data.toString())
                    loadImage("", data.data)
                    imageUri = data.data!!
                    Log.i("ISI IMAGE URI", imageUri.toString())
                }
            }

            imageFile = convertToFile()
            Log.i("ISI FILE", imageFile.toString())
            updateData(image = imageFile)
        } else {
            Log.e("ERROR GETTING FILE", "hadeh")
        }
    }

    private fun setData() {
        viewModel.getUserProfile().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.i("ISI IMAGE URL", user.imageUrl ?: "gak ada")
//                loadImage(user.imageUrl!!)
                binding.etName.setText(user.name)
                binding.etEmail.setText(user.email)
                binding.etPhone.setText(user.phone)
                binding.etBirthDate.setText(user.birth)
                binding.etAddress.setText(user.address)
            }
        }
    }

    private fun loadImage(image: String, uri: Uri? = null) {
        Log.i("PASS URI", uri?.toString() ?: "gak masuk uri ne null")
        Glide.with(this)
            .load(uri ?: image)
            .override(1600, 1600)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.profileProgressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.profileProgressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.avatar)
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
            showCustomDatePicker()
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
        binding.etAddress.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etAddress.focusable = View.NOT_FOCUSABLE
                binding.etAddress.isFocusableInTouchMode = false

                updateData(address = binding.etAddress.text.toString())
            }
        }
    }

    private fun showCustomDatePicker() {
        val dialog = Dialog(requireContext())
        var day = ""
        var month = ""
        var year = ""

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_custom_datepicker)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val saveBtn: MaterialButton = dialog.findViewById(R.id.getDateBtn)
        val dayNumPick: NumberPicker? = dialog.findViewById(R.id.dayNumPick)
        val monthNumPick: NumberPicker? = dialog.findViewById(R.id.monthNumPick)
        val yearNumPick: NumberPicker? = dialog.findViewById(R.id.yearNumPick)
        val nowYear = getYear().toInt()
        val monthVals = arrayOf<String>("Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember")

        dayNumPick?.minValue = 1
        dayNumPick?.maxValue = 31
        monthNumPick?.minValue = 1
        monthNumPick?.maxValue = 12
        monthNumPick?.displayedValues = monthVals
        yearNumPick?.minValue = nowYear - 100
        yearNumPick?.maxValue = nowYear

        saveBtn.setOnClickListener {
            day = dayNumPick?.value.toString()
            month = monthNumPick?.value.toString()
            year = yearNumPick?.value.toString()

            binding.etBirthDate.setText("$day-$month-$year")

            dialog.dismiss()

            updateData(birth = binding.etBirthDate.text.toString())
        }

        dialog.show()
    }

    private fun showImageDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_image_picker_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cameraBtn: MaterialButton = dialog.findViewById(R.id.btnCamera)
        val galleryBtn: MaterialButton = dialog.findViewById(R.id.btnGallery)

        cameraBtn.setOnClickListener {
            openCamera(dialog)
        }
        galleryBtn.setOnClickListener {
            openGallery(dialog)
        }

        dialog.show()
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    CAMERA,
                    READ_MEDIA_IMAGES
                ),
                REQUEST_CODE_PERMISSION
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(
                arrayOf(
                    CAMERA,
                    READ_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION
            )
        } else {
            requestPermissions(
                arrayOf(
                    CAMERA,
                    WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun openGallery(dialog: Dialog) {
        dialog.dismiss()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun openCamera(dialog: Dialog) {
        dialog.dismiss()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            var photoFile: File? = null

            try {
                photoFile = createCustomTempFile()
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
                startActivityForResult(intent, REQUEST_CODE_CAMERA)
            }
        }
    }

    private fun convertToFile() : File {
        val myFile = createCustomTempFile()

        val outputStream = FileOutputStream(myFile)
        val inputStream = requireContext().getContentResolver().openInputStream(imageUri)

        if (inputStream != null) {
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        }
        outputStream.flush()

        binding.avatar.setImageURI(imageUri)

        return myFile
    }

    private fun createCustomTempFile(): File {
        val filesDir = requireContext().externalCacheDir
        return File.createTempFile(getTimeStamp(), ".jpg", filesDir)
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
//                    loadImage(updatedUser.imageUrl!!)
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