package com.team6.connectbca.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.team6.connectbca.databinding.ActivityPinBinding

class PinActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPinBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}