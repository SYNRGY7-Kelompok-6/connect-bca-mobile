package com.team6.connectbca.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.databinding.ActivityLoginBinding
import com.team6.connectbca.ui.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

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
                Snackbar.make(binding.root, "Username atau password Anda salah", Snackbar.LENGTH_SHORT).show()
                binding.etUserId.text?.clear()
                binding.etPassword.text?.clear()
            }
        }

        viewModel.getSuccess().observe(this) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.root, "Login Berhasil!", Snackbar.LENGTH_SHORT).show()
                MainActivity.startActivity(this)
                finish();
            }
        }
    }

    private fun checkUserInput(userId: String, pass: String) : Boolean {
        return ((!userId.isNullOrEmpty()) && (!pass.isNullOrEmpty()))
    }
}