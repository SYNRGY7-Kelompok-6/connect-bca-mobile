package com.team6.connectbca.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityMainBinding
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
        setupNavigationComponent()
        setupBottomNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(binding.navGraph.id) as NavHostFragment
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun setupNavigationComponent() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(binding.navGraph.id) as NavHostFragment
        navController = host.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.i("CURRENT FRAGMENT", destination.id.toString())
            Log.i("HOME FRAGMENT", R.id.homeFragmentRoot.toString())
            when (destination.id) {
                R.id.homeFragmentRoot -> showBottomNav()
                R.id.notificationFragment -> showBottomNav()
                R.id.promoFragment -> showBottomNav()
                R.id.profileFragment -> showBottomNav()
                2131296618 -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.navGraph.id, fragment)
        transaction.commit()
    }

    private fun setupBottomNav() {
        binding.homeBottomNav.setOnItemSelectedListener {menuItem ->
            when(menuItem.itemId) {
                R.id.home -> {
                    startActivity(this)
                    true
                }
                R.id.promo -> {
                    loadFragment(PromoFragment())
                    true
                }
                R.id.notification -> {
                    loadFragment(NotificationFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> {
                    false
                }
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