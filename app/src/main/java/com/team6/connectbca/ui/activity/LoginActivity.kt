package com.team6.connectbca.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team6.connectbca.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}