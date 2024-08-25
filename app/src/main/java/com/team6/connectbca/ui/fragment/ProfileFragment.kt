package com.team6.connectbca.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
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
import com.team6.connectbca.extensions.getYear
import com.team6.connectbca.ui.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModel<ProfileViewModel>()

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

        viewModel.getSuccess().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                showSnackbar("Update profil berhasil")
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                showSnackbar("Gagal memuat data profile")
            }
        }
    }

    private fun setData() {
        viewModel.getUserProfile().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                loadImage(user.imageUrl!!)
                binding.etName.setText(user.name)
                binding.etEmail.setText(user.email)
                binding.etPhone.setText(user.phone)
                binding.etBirthDate.setText(user.birth)
                binding.etAddress.setText(user.address)
            }
        }
    }

    private fun loadImage(image: String, uri: Uri? = null) {
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

    private fun updateData(
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        birth: String? = null,
        address: String? = null
    ) {
        viewModel.updateUserProfile(
            name,
            email,
            phone,
            birth,
            address
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

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}