package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.databinding.FragmentTransferBinding
import com.team6.connectbca.ui.fragment.adapter.TransferTabPagerAdapter

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTransferBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabLayout()
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Transfer"
    }

    private fun setUpTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = TransferTabPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Daftar Favorit" else "Transfer Terjadwal"
        }.attach()
    }
}