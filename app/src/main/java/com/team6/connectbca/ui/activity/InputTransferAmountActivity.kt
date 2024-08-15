package com.team6.connectbca.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityInputTransferAmountBinding
import com.team6.connectbca.ui.fragment.adapter.InputTransferAmountTabPagerAdapter
import com.team6.connectbca.ui.fragment.adapter.TransferTabPagerAdapter

class InputTransferAmountActivity : AppCompatActivity() {

    private val binding by lazy { ActivityInputTransferAmountBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = InputTransferAmountTabPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Sekarang" else "Atur Tanggal"
        }.attach()
    }
}