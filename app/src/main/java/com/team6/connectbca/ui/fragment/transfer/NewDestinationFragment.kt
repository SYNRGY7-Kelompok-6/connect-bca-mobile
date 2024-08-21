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
import com.team6.connectbca.ui.viewmodel.ShowQrViewModel
import com.team6.connectbca.ui.viewmodel.TransferNewDestinationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewDestinationFragment : Fragment() {

    private var _binding: FragmentNewDestinationBinding? = null
    private val binding get() = _binding!!
    private var selectedBankName: String? = null
    private var accountNumber: String? = null
    private val savedAccountViewModel by viewModel<SavedAccountViewModel>()
    private val newDestinationViewModel by viewModel<TransferNewDestinationViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewDestinationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            "requestKey",
            viewLifecycleOwner
        ) { _, bundle ->
            selectedBankName = bundle.getString("selectedBankName")
            binding.bankNameBtn.text = selectedBankName ?: "Nama Bank Tujuan"
        }

        setupUI()
    }


    private fun setupUI() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.setNavigationOnClickListener { navController.popBackStack() }
        binding.toolbar.title = "Transfer ke Tujuan Baru"

        setupBankNameButtonBinding()
        setupContinueButtonBinding()

        accountNumber = arguments?.getString("accountNumber")
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
                Toast.makeText(context, "Nomor rekening tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
            } else {
                validateAccountNumber(accountNumber)
            }
        }
    }

    private fun validateAccountNumber(accountNumber: String) {
        savedAccountViewModel.saveAccount(accountNumber).observe(viewLifecycleOwner) { result ->
            Log.d("NewDestinationFragment", "Result: $result")
            when (result) {
                is SaveAccountUseCase.SaveAccountInfo.Success -> {
                    val action = NewDestinationFragmentDirections
                        .actionNewDestinationFragmentToRecepientDetailFragment(
                            result.savedAccount.data?.savedBeneficiaryId,
                            result.savedAccount.data?.beneficiaryAccountNumber,
                            result.savedAccount.data?.beneficiaryAccountName,
                            false
                        )
                    findNavController().navigate(action)
                }

                is SaveAccountUseCase.SaveAccountInfo.Failure -> {
                    Log.e("NewDestinationFragment", "Error: ${result.errorMessage}")
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
