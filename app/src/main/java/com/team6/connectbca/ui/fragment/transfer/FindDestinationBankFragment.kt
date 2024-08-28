package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.databinding.FragmentFindDestinationBankBinding
import com.team6.connectbca.ui.fragment.adapter.transfer.DestinationBankAdapter

class FindDestinationBankFragment : Fragment() {

    private var _binding: FragmentFindDestinationBankBinding? = null
    private val binding get() = _binding!!
    private lateinit var destinationBankAdapter: DestinationBankAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindDestinationBankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        setupMainBankItem()
        setupSearchView()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Pilih Bank Tujuan"
        binding.toolbar.setNavigationOnClickListener { navigateBack() }
    }

    private fun setupMainBankItem() {
        val mainBankItem = binding.itemBCA

        mainBankItem.root.setOnClickListener {
            val selectedBankName = mainBankItem.tvDestinationBankName.text.toString()

            // Mengirim data kembali ke NewDestinationFragment
            val result = Bundle().apply {
                putString("selectedBankName", selectedBankName)
            }
            parentFragmentManager.setFragmentResult("requestKey", result)

            // Pop fragment ini dari back stack
            findNavController().popBackStack()
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(
            object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

        binding.cancelButton.setOnClickListener {
            binding.searchView.setQuery("", false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateBack() {
        val action = FindDestinationBankFragmentDirections
            .actionFindDestinationBankFragmentToNewDestinationFragment(null, null)
        findNavController().navigate(action)
    }
}