package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentInputTransferAmountBinding
import com.team6.connectbca.databinding.FragmentTransferBinding
import com.team6.connectbca.ui.fragment.adapter.InputTransferAmountTabPagerAdapter

class InputTransferAmountFragment : Fragment() {
    private lateinit var binding: FragmentInputTransferAmountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentInputTransferAmountBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
    }

    private fun setupTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = InputTransferAmountTabPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Sekarang" else "Atur Tanggal"
        }.attach()
    }
}