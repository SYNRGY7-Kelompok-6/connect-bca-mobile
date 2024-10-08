package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.team6.connectbca.databinding.FragmentFavoritesTransferBinding
import com.team6.connectbca.domain.model.SavedAccountData
import com.team6.connectbca.ui.fragment.adapter.transfer.favoritedestination.FavoriteDestinationAdapterListener
import com.team6.connectbca.ui.fragment.adapter.transfer.favoritedestination.FavoritesDestinationAdapter
import com.team6.connectbca.ui.viewmodel.SavedAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTransferFragment : Fragment(), FavoriteDestinationAdapterListener {
    private var _binding: FragmentFavoritesTransferBinding? = null
    private val binding get() = _binding!!
    private val savedAccountViewModel by viewModel<SavedAccountViewModel>()
    private val favoritesAdapter = FavoritesDestinationAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeSavedAccounts()
        setupSearchView()
        setupAddNewRecipientButton()

        savedAccountViewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.transferProgressBar.visibility = View.VISIBLE
            } else {
                binding.transferProgressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickDestination(savedAccount: SavedAccountData) {
        val action = TransferFragmentDirections.actionTransferFragmentToRecepientDetailFragment(
            savedAccount.savedBeneficiaryId!!,
            savedAccount.beneficiaryAccountNumber,
            savedAccount.beneficiaryAccountName,
            savedAccount.favorite ?: false
        )
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { savedAccountViewModel.getSavedAccounts(it, true) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { savedAccountViewModel.getSavedAccounts(it, true) }
                return true
            }
        })
    }

    private fun setupAddNewRecipientButton() {
        binding.addNewRecipientText.setOnClickListener {
            val action = TransferFragmentDirections
                .actionTransferFragmentToNewDestinationFragment(null, null)
            findNavController().navigate(action)
        }
        binding.addNewRecipientButton.setOnClickListener {
            val action = TransferFragmentDirections
                .actionTransferFragmentToNewDestinationFragment(null, null)
            findNavController().navigate(action)
        }
    }

    private fun observeSavedAccounts() {
        savedAccountViewModel.getSavedAccounts("", false)
            .observe(viewLifecycleOwner) { savedAccounts ->
                savedAccounts.data?.let { accounts ->
                    favoritesAdapter.submitList(accounts)
                }
            }
    }
}