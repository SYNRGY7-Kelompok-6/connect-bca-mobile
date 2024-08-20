package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team6.connectbca.databinding.FragmentInputTransferAmountBinding
import com.team6.connectbca.databinding.FragmentInputTransferNowAmountBinding

class InputTransferNowAmountFragment : Fragment() {
    private var _binding: FragmentInputTransferNowAmountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputTransferNowAmountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}