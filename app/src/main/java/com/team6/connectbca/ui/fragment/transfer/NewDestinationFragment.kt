package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentNewDestinationBinding
import com.team6.connectbca.domain.usecase.SaveAccountUseCase
import com.team6.connectbca.ui.viewmodel.SavedAccountViewModel

class NewDestinationFragment : Fragment() {

    private var _binding: FragmentNewDestinationBinding? = null
    private val binding get() = _binding!!
    private val savedAccountViewModel: SavedAccountViewModel by lazy {
        ViewModelProvider(this)[SavedAccountViewModel::class.java]
    }
    private var selectedBankName: String? = null
    private var accountNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewDestinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Transfer ke Tujuan Baru"

        setupBankNameButtonBinding()
        setupContinueButtonBinding()
        setupDefaultAccountNumber()

        arguments?.let {
            selectedBankName = it.getString("selectedBankName")
            accountNumber = it.getString("accountNumber")
        }

        binding.bankNameButton.text = selectedBankName ?: "Nama Bank Tujuan"
        binding.accountNumberEditText.setText(accountNumber)
    }

    private fun setupBankNameButtonBinding() {
        binding.bankNameButton.setOnClickListener {
            findNavController().navigate(R.id.action_newDestinationFragment_to_findDestinationBankFragment)
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
                    Toast.makeText(context, "Nomor rekening valid", Toast.LENGTH_SHORT).show()
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

    private fun setupDefaultAccountNumber() {/*TODO*/}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
