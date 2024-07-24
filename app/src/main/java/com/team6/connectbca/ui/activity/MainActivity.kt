package com.team6.connectbca.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityMainBinding
import com.team6.connectbca.databinding.CustomerBankCardBinding
import com.team6.connectbca.ui.fragment.TabPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Informasi Saldo"
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        setupTabLayout()
        setupBottomSheet()

        val customerBankcard = binding.root.findViewById<CardView>(R.id.cardCustomer)
        val customerBankcardBinding = CustomerBankCardBinding.bind(customerBankcard)
        customerBankcardBinding.iconButtonCopy.setOnClickListener {
            copyToClipboard(customerBankcardBinding.tvCardNumber.text.toString())
        }
    }

    private fun setupTabLayout() {
        val adapter = TabPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Hari Ini"
                1 -> "Bulan"
                2 -> "Cari"
                else -> null
            }
        }.attach()
    }

    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        val screenHeight = resources.displayMetrics.heightPixels

        val peekHeight = when {
            screenHeight <= 480 -> screenHeight * 0.15
            screenHeight <= 800 -> screenHeight * 0.2
            screenHeight <= 1024 -> screenHeight * 0.25
            screenHeight <= 1280 -> screenHeight * 0.3
            screenHeight <= 1440 -> screenHeight * 0.35
            screenHeight <= 1920 -> screenHeight * 0.4
            else -> screenHeight * 0.45
        }

        bottomSheetBehavior.peekHeight = peekHeight.toInt()

        val maxHeight = (screenHeight * 1.0).toInt()
        bottomSheetBehavior.maxHeight = maxHeight
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clip)
        showToast("Text copied to clipboard")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
