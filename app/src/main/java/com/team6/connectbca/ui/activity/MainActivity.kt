package com.team6.connectbca.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityMainBinding
import com.team6.connectbca.ui.fragment.HomeFragment
import com.team6.connectbca.ui.fragment.NotificationFragment
import com.team6.connectbca.ui.fragment.ProfileFragment
import com.team6.connectbca.ui.fragment.PromoFragment

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
        setContentView(binding.root)

        // Setup Navigation Component
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_graph) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.homeBottomNav, navController)

        // Optional: Hide/Show BottomNavigationView based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.notificationFragment, R.id.promoFragment, R.id.profileFragment -> showBottomNav()

                else -> hideBottomNav()
            }
        }
        binding.homeBottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.notification -> {
                    navController.navigate(R.id.notificationFragment)
                    true
                }
                R.id.promo -> {
                    navController.navigate(R.id.promoFragment)
                    true
                }
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun showBottomNav() {
        binding.homeBottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.homeBottomNav.visibility = View.GONE
    }
}