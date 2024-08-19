package com.team6.connectbca.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityNewDestinationBinding

class NewDestinationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewDestinationBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigationComponent()
    }

    private fun setupNavigationComponent() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}