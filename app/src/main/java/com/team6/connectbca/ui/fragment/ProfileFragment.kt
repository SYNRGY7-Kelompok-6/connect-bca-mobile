package com.team6.connectbca.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentHomeBinding
import com.team6.connectbca.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

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
        setupEditButtonsListener()
        setupEditTextOnFocusChangedListener()
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEditTextOnFocusChangedListener() {
        binding.etName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etName.focusable = View.NOT_FOCUSABLE
                binding.etName.isFocusableInTouchMode = false
            }
        }
        binding.etEmail.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etEmail.focusable = View.NOT_FOCUSABLE
                binding.etEmail.isFocusableInTouchMode = false
            }
        }
        binding.etPhone.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etPhone.focusable = View.NOT_FOCUSABLE
                binding.etPhone.isFocusableInTouchMode = false
            }
        }
        binding.etBirthDate.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etBirthDate.focusable = View.NOT_FOCUSABLE
                binding.etBirthDate.isFocusableInTouchMode = false
            }
        }
        binding.etAddress.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etAddress.focusable = View.NOT_FOCUSABLE
                binding.etAddress.isFocusableInTouchMode = false
            }
        }
    }
}