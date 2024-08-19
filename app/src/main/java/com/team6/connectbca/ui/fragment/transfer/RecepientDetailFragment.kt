package com.team6.connectbca.ui.fragment.transfer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.databinding.FragmentRecepientDetailBinding
import com.team6.connectbca.ui.activity.InputTransferAmountActivity

class RecepientDetailFragment : Fragment() {

    private var _binding: FragmentRecepientDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecepientDetailBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Cek Detail Penerima"

        setupContinueButtonBinding()
    }

    private fun setupContinueButtonBinding() {
        binding.continueButton.setOnClickListener {
            val intent = Intent(requireContext(), InputTransferAmountActivity::class.java)
            startActivity(intent)
        }
    }
}