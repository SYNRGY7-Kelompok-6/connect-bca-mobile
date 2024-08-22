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
        setupNavigationComponent()
        setupBottomNav()
        showBottomNav()
    }

    override fun onSupportNavigateUp(): Boolean {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(binding.navGraph.id) as NavHostFragment
        return host.navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun setupNavigationComponent() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(binding.navGraph.id) as NavHostFragment
        navController = host.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNav()
                R.id.notificationFragment -> showBottomNav()
                R.id.promoFragment -> showBottomNav()
                R.id.profileFragment -> showBottomNav()
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
                    loadFragment(HomeFragment())
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