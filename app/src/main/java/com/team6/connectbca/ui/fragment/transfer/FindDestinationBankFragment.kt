package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.team6.connectbca.databinding.FragmentFindDestinationBankBinding
import com.team6.connectbca.ui.fragment.adapter.DestinationBankAdapter
import com.team6.connectbca.ui.viewmodel.SavedAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FindDestinationBankFragment : Fragment() {

    private var _binding: FragmentFindDestinationBankBinding? = null
    private val binding get() = _binding!!

    private lateinit var destinationBankAdapter: DestinationBankAdapter
    private val savedAccountViewModel: SavedAccountViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindDestinationBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMainBankItem()
        setupRecyclerView()
        setupSearchView()
        observeSavedAccounts()
    }

    private fun setupMainBankItem() {
        val mainBankItem = binding.itemBCA
        mainBankItem.tvDestinationBankName.text = "Connect"

        mainBankItem.root.setOnClickListener {
            val selectedBankName = mainBankItem.tvDestinationBankName.text.toString()

            findNavController().previousBackStackEntry
                ?.savedStateHandle
                ?.set("selectedBankName", selectedBankName)

            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        destinationBankAdapter = DestinationBankAdapter(emptyList()) { selectedBank ->
            val action = FindDestinationBankFragmentDirections
                .actionFindDestinationBankFragmentToNewDestinationFragment(
                    selectedBank.beneficiaryAccountName ?: "BCA"
                )
            findNavController().navigate(action)
        }

        binding.rvAnotherDestinationBank.apply {
            adapter = destinationBankAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    destinationBankAdapter.filter.filter(newText)
                    return true
                }
            })

        binding.cancelButton.setOnClickListener {
            binding.searchView.setQuery("", false)
        }
    }

    private fun observeSavedAccounts() {
        savedAccountViewModel.getSavedAccounts("", false).observe(viewLifecycleOwner) { savedAccounts ->
            savedAccounts.data?.let { accounts ->
                destinationBankAdapter.updateData(accounts)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}