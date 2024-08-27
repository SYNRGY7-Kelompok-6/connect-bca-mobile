package com.team6.connectbca.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData() {
        viewModel.getUserProfile().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.etName.setText(user.name)
                binding.etEmail.setText(user.email)
                binding.etPhone.setText(user.phone)
                binding.etBirthDate.setText(user.birth)
                binding.etAddress.setText(user.address)

                setupEditTextOnFocusChangedListener(
                    mapOf(
                        "name" to user.name!!,
                        "email" to user.email!!,
                        "phone" to user.phone!!,
                        "birth" to user.birth!!,
                        "address" to user.address!!
                    )
                )
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
            showCustomDatePicker()
        }
        binding.btnAddressEdit.setOnClickListener {
            binding.etAddress.focusable = View.FOCUSABLE
            binding.etAddress.isFocusableInTouchMode = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEditTextOnFocusChangedListener(former: Map<String, String>) {
        binding.etName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etName.focusable = View.NOT_FOCUSABLE
                binding.etName.isFocusableInTouchMode = false
                checkInputData(binding.etName.text.toString(), former.getValue("name"), "name")
            }
        }
        binding.etEmail.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etEmail.focusable = View.NOT_FOCUSABLE
                binding.etEmail.isFocusableInTouchMode = false
                checkInputData(binding.etEmail.text.toString(), former.getValue("email"), "email")
            }
        }
        binding.etPhone.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etPhone.focusable = View.NOT_FOCUSABLE
                binding.etPhone.isFocusableInTouchMode = false
                checkInputData(binding.etPhone.text.toString(), former.getValue("phone"), "phone")
            }
        }
        binding.etAddress.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etAddress.focusable = View.NOT_FOCUSABLE
                binding.etAddress.isFocusableInTouchMode = false
                checkInputData(binding.etAddress.text.toString(), former.getValue("address"), "address")
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
        yearNumPick?.maxValue = nowYear - 17

        saveBtn.setOnClickListener {
            day = dayNumPick?.value.toString()
            month = monthNumPick?.value.toString()
            year = yearNumPick?.value.toString()

            binding.etBirthDate.setText("$day-$month-$year")

            dialog.dismiss()

            checkInputData(binding.etBirthDate.text.toString(), "", "birth")
        }

        dialog.show()
    }

    private fun checkInputData(input: String, former: String, type: String) {
        if (input.isNullOrEmpty() || input == "") {
            when(type) {
                "name" -> {
                    binding.etName.setText(former)
                    showSnackbar("Nama tidak boleh kosong")
                }
                "email" -> {
                    binding.etEmail.setText(former)
                    showSnackbar("Email tidak boleh kosong")
                }
                "phone" -> {
                    binding.etPhone.setText(former)
                    showSnackbar("Nomor telepon tidak boleh kosong")
                }
                "birth" -> {
                    binding.etBirthDate.setText(former)
                    showSnackbar("Tanggal lahir tidak boleh kosong")
                }
                "address" -> {
                    binding.etAddress.setText(former)
                    showSnackbar("Alamat tidak boleh kosong")
                }
            }
        } else {
            validateInput(input, former, type)
        }
    }

    private fun validateInput(input: String, former: String, type: String) {
        when(type) {
            "name" -> {
                if (isAlpha(input)) {
                    updateData(name = input)
                } else {
                    binding.etName.setText(former)
                    showSnackbar("Nama tidak boleh mengandung angka/karakter")
                }
            }
            "email" -> {
                if (isEmail(input)) {
                    updateData(email = input)
                } else {
                    binding.etEmail.setText(former)
                    showSnackbar("Data harus berupa email")
                }
            }
            "phone" -> {
                if (isDigit(input)) {
                    updateData(phone = input)
                } else {
                    binding.etPhone.setText(former)
                    showSnackbar("Nomor telepon harus berupa angka")
                }
            }
            "birth" -> {
                updateData(birth = input)
            }
            "address" -> {
                updateData(address = input)
            }
        }
    }

    private fun isAlpha(text: String) : Boolean {
        var trimmed = text.replace(".", "")
        trimmed = trimmed.replace("-", "")
        trimmed = trimmed.replace(" ", "")

        return trimmed.all { it.isLetter() }
    }

    private fun isEmail(text: String) : Boolean {
        return text.contains("@")
    }

    private fun isDigit(text: String) : Boolean {
        return text.all { it.isDigit() }
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