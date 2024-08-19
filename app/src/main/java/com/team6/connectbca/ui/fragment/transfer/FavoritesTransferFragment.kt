package com.team6.connectbca.ui.fragment.transfer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.team6.connectbca.databinding.FragmentFavoritesTransferBinding
import com.team6.connectbca.ui.fragment.adapter.FavoritesDestinationAdapter
import com.team6.connectbca.ui.viewmodel.SavedAccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTransferFragment : Fragment() {
    private var _binding: FragmentFavoritesTransferBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoritesAdapter: FavoritesDestinationAdapter
    private val savedAccountViewModel: SavedAccountViewModel by viewModel()

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
        setupSearchView()
        setupAddNewRecipientButton()
        observeSavedAccounts()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesDestinationAdapter(emptyList()) { savedAccount ->

            // TODO:  Handle favorite destination click
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
        binding.addNewRecipientButton.setOnClickListener {
            val intent = Intent(requireContext(), NewDestinationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeSavedAccounts() {
        savedAccountViewModel.getSavedAccounts("", true).observe(viewLifecycleOwner) {
            savedAccounts -> savedAccounts.data?.let { favoritesAdapter.updateFavorites(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}