package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentNewDestinationBinding
import com.team6.connectbca.domain.usecase.SaveAccountUseCase
import com.team6.connectbca.ui.fragment.adapter.DestinationBankAdapter
import com.team6.connectbca.ui.viewmodel.SavedAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewDestinationFragment : Fragment() {

    private var _binding: FragmentNewDestinationBinding? = null
    private val binding get() = _binding!!
    private var selectedBankName: String? = null
    private var accountNumber: String? = null
    private val savedAccountViewModel by viewModel<SavedAccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewDestinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = NewDestinationFragmentDirections.actionNewDestinationFragmentToTransferFragment()
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigate(action) }
        binding.toolbar.title = "Transfer ke Tujuan Baru"

        setupBankNameButtonBinding()
        setupContinueButtonBinding()

        arguments?.let {
            selectedBankName = it.getString("selectedBankName")
            accountNumber = it.getString("accountNumber")
        }

        binding.bankNameBtn.text = selectedBankName ?: "Nama Bank Tujuan"
        binding.accountNumberEditText.setText(accountNumber ?: "")
    }

    private fun setupBankNameButtonBinding() {
        binding.bankNameBtn.setOnClickListener {
            val action = NewDestinationFragmentDirections
                .actionNewDestinationFragmentToFindDestinationBankFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupContinueButtonBinding() {
        binding.continueButton.setOnClickListener {
            val accountNumber = binding.accountNumberEditText.text.toString()

            if (selectedBankName.isNullOrEmpty()) {
                Toast.makeText(context, "Silakan pilih bank tujuan", Toast.LENGTH_SHORT).show()
            } else if (accountNumber.isEmpty()) {
                Toast.makeText(context, "Nomor rekening tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                validateAccountNumber(accountNumber)
            }
        }
    }

    private fun validateAccountNumber(accountNumber: String) {
        savedAccountViewModel.saveAccount(accountNumber).observe(viewLifecycleOwner) { result ->
            when (result) {
                is SaveAccountUseCase.SaveAccountInfo.Success -> {
                    val action = NewDestinationFragmentDirections
                        .actionNewDestinationFragmentToRecepientDetailFragment(
                            null, accountNumber, selectedBankName)
                    findNavController().navigate(action)
                }

                is SaveAccountUseCase.SaveAccountInfo.Failure -> {
                    Toast.makeText(
                        context,
                        "Nomor rekening tidak valid: ${result.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
