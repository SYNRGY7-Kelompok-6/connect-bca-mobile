package com.team6.connectbca.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.databinding.ActivityLoginBinding
import com.team6.connectbca.databinding.ActivityMainBinding
import com.team6.connectbca.ui.fragment.TabPagerAdapter

class MainActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navGraph.id) as NavHostFragment

        navController = navHostFragment.navController
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.apply {
//            title = "Informasi Saldo"
//            setDisplayHomeAsUpEnabled(false)
//            setDisplayShowHomeEnabled(false)
//        }
//        setupTabLayout()
    }

//    private fun setupTabLayout() {
//        val adapter = TabPagerAdapter(supportFragmentManager, lifecycle)
//        binding.viewPager.adapter = adapter
//
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = when (position) {
//                0 -> "Hari Ini"
//                1 -> "Bulan"
//                2 -> "Cari"
//                else -> null
//            }
//        }.attach()
//    }
}