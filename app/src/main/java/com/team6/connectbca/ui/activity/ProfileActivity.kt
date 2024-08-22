package com.team6.connectbca.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.team6.connectbca.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }

    private val binding by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupNavigationComponent()
    }

    override fun onSupportNavigateUp(): Boolean {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(binding.profileFragmentContainer.id) as NavHostFragment
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupNavigationComponent() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(binding.profileFragmentContainer.id) as NavHostFragment
        navController = host.navController
    }
}