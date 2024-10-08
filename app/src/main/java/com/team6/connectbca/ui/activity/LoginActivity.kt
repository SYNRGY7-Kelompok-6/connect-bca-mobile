package com.team6.connectbca.ui.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.ActivityLoginBinding
import com.team6.connectbca.ui.viewmodel.AuthViewModel
import com.team6.connectbca.ui.viewmodel.ConnectivityStatusLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<AuthViewModel>()
    private lateinit var networkStatusLiveData: ConnectivityStatusLiveData
    private var isDisconnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        networkStatusLiveData = ConnectivityStatusLiveData(applicationContext)

        checkConnectivity()
        observeViewModel()
        setButtonsOnClickListener()
    }

    private fun checkConnectivity() {
        networkStatusLiveData.observe(this, Observer { isConnected ->
            if (isConnected && isDisconnected) {
                isDisconnected = false
                showSnackbar("Koneksi Internet Tersambung Kembali")
            } else if (!isConnected) {
                isDisconnected = true
                showSnackbar("Maaf, Koneksi Internet Anda Terputus")
            }
        })
    }

    private fun observeViewModel() {
        viewModel.getUserSessionData().observe(this) { user ->
            if (user.isNullOrEmpty()) {
                binding.tilUserId.visibility = View.VISIBLE
            } else {
                if (user.getValue("userId") != "") {
                    binding.tilUserId.visibility = View.GONE
                    (binding.etUserId as TextView).text = user.getValue("userId").toString()
                }
            }
        }

        viewModel.getLoading().observe(this) { isLoading ->
            if (isLoading) {
                binding.loginProgressBar.visibility = View.VISIBLE
            } else {
                binding.loginProgressBar.visibility = View.GONE
            }
        }

        viewModel.getError().observe(this) { error ->
            if (error != null) {
                binding.etUserId.error = "User Id Anda sepertinya salah"
                binding.etPassword.error = "Password Anda sepertinya salah"
            }
        }

        viewModel.getSuccess().observe(this) { isSuccess ->
            if (isSuccess) {
                MainActivity.startActivity(this)
                finish();
            }
        }
    }

    private fun setButtonsOnClickListener() {
        binding.btnLogin.setOnClickListener {
            val userId = binding.etUserId.text.toString()
            val pass = binding.etPassword.text.toString()
            val checkResult = checkUserInput(userId, pass)

            if (userId.isNullOrEmpty()) { binding.etUserId.error = "User Id tidak boleh kosong" }
            if (pass.isNullOrEmpty()) { binding.etPassword.error = "Password tidak boleh kosong" }
            if (checkResult) {
                binding.etUserId.error = null
                binding.etPassword.error = null

                viewModel.userLogin(userId, pass)
            }
        }

        binding.tvForgotPassword.setOnClickListener { showForgetPasswordAlertDialog() }

        binding.btnWallet.setOnClickListener { showQuickAccessAlertDialog() }
        binding.btnQris.setOnClickListener { showQuickAccessAlertDialog() }
        binding.btnTransfer.setOnClickListener { showQuickAccessAlertDialog() }
    }

    private fun checkUserInput(userId: String, pass: String) : Boolean {
        return ((!userId.isNullOrEmpty()) && (!pass.isNullOrEmpty()))
    }

    private fun showForgetPasswordAlertDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_forget_password_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeBtn: MaterialButton = dialog.findViewById(R.id.alertCloseBtn)
        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showQuickAccessAlertDialog() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_quick_access_notfound_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeBtn: MaterialButton = dialog.findViewById(R.id.quickAccessAlertCloseBtn)
        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
