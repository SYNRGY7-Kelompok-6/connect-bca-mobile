package com.team6.connectbca.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.databinding.ActivityTransferBinding
import com.team6.connectbca.ui.fragment.adapter.TransferTabPagerAdapter

class TransferActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTransferBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = TransferTabPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Daftar Favorit" else "Transfer Terjadwal"
        }.attach()
    }
}