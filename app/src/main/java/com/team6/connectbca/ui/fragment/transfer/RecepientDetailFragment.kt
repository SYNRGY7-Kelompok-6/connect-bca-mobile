package com.team6.connectbca.ui.fragment.transfer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.databinding.FragmentRecepientDetailBinding
import com.team6.connectbca.ui.fragment.HomeFragmentDirections

class RecepientDetailFragment : Fragment() {

    private var _binding: FragmentRecepientDetailBinding? = null
    private val binding get() = _binding!!
    private var savedAccountId: String? = null
    private var beneficiaryAccountNo: String? = null
    private var beneficiaryBank: String? = null

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

        arguments?.let {
            savedAccountId = it.getString("savedAccountId")
            beneficiaryBank = it.getString("beneficiaryBank")
            beneficiaryAccountNo = it.getString("beneficiaryAccountNo")
        }

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Cek Detail Penerima"

        setData()
        setupContinueButtonBinding()
    }

    private fun setData() {
        binding.logoText.text = if (savedAccountId.isNullOrEmpty()) {
            getAcronym("Pengguna")
        } else {
            getAcronym("ini kudu get account by id dulu")
        }
    }

    private fun setupContinueButtonBinding() {
        val action = RecepientDetailFragmentDirections.actionRecepientDetailFragmentToInputTransferAmountFragment()
        binding.continueButton.setOnClickListener {
            findNavController().navigate(action)
        }
    }

    private fun getAcronym(word: String) : String {
        return if (word.length >= 2) {
            val trimmedString = word.substring(2,word.length)
            word.replace(trimmedString, "").uppercase()
        } else {
            word.uppercase()
        }
    }
}