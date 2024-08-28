package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentRecepientDetailBinding

class RecepientDetailFragment : Fragment() {

    private var _binding: FragmentRecepientDetailBinding? = null
    private val binding get() = _binding!!
    private var savedAccountId: String? = null
    private var beneficiaryAccountNo: String? = null
    private var beneficiaryBank: String? = null
    private var isFavorite: Boolean = false

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
        Log.d("RecepientDetailFragment", "${arguments}")
        arguments?.let {
            savedAccountId = it.getString("savedBeneficiaryId")
            beneficiaryBank = it.getString("beneficiaryBank")
            beneficiaryAccountNo = it.getString("beneficiaryAccountNo")
            isFavorite = it.getBoolean("isFavorite")
            Log.d("RecepientDetailFragment", "savedAccountId: $savedAccountId, beneficiaryBank: $beneficiaryBank, beneficiaryAccountNo: $beneficiaryAccountNo")
        }

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Cek Detail Penerima"
        binding.toolbar.background = resources.getDrawable(android.R.color.transparent)

        setData()
        setupContinueButtonBinding()
    }

    private fun setData() {
        binding.logoText.text = if (savedAccountId.isNullOrEmpty()) {
            getAcronym("Pengguna")
        } else {
            getAcronym(beneficiaryBank!!)

        }
        binding.recipientName.text = beneficiaryBank
        binding.recipientAccountNumber.text = beneficiaryAccountNo
        binding.recipientAccountNumber.contentDescription = beneficiaryAccountNo?.split("")?.joinToString(" ")
        if (isFavorite) binding.heartIcon.setImageResource(R.drawable.ic_heart) else binding.heartIcon.setImageResource(R.drawable.ic_heart_outline)
    }

    private fun setupContinueButtonBinding() {
        val action = RecepientDetailFragmentDirections.actionRecepientDetailFragmentToInputTransferAmountFragment(
            savedAccountId,
            beneficiaryAccountNo,
            beneficiaryBank
        )
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