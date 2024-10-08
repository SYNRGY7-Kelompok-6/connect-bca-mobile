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
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityMainBinding
import com.team6.connectbca.ui.viewmodel.ConnectivityStatusLiveData


class MainActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController
    private lateinit var networkStatusLiveData: ConnectivityStatusLiveData
    private var isDisconnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        networkStatusLiveData = ConnectivityStatusLiveData(applicationContext)

        checkConnectivity()

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

    private fun checkConnectivity() {
        networkStatusLiveData.observe(this, Observer { isConnected ->
            if (isConnected && isDisconnected) {
                isDisconnected = false
                Snackbar.make(binding.root, "Koneksi Internet Tersambung Kembali", Snackbar.LENGTH_SHORT).show()
            } else if (!isConnected) {
                isDisconnected = true
                Snackbar.make(binding.root, "Maaf, Koneksi Internet Anda Terputus", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun showBottomNav() {
        binding.homeBottomNav.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.homeBottomNav.visibility = View.GONE
    }
}
