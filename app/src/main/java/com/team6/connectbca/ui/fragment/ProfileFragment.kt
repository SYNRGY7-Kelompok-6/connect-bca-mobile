package com.team6.connectbca.ui.fragment

import android.os.Build
import android.os.Bundle
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
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEditTextOnFocusChangedListener() {
        binding.etName.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                binding.etName.focusable = View.NOT_FOCUSABLE
            }
        }
    }
}