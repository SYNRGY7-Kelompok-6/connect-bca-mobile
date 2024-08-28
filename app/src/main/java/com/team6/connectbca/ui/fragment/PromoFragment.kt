package com.team6.connectbca.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentPromoBinding

class PromoFragment : Fragment() {
    private lateinit var binding: FragmentPromoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPromoBinding.inflate(inflater, container, false)
        androidBackButton()
        return binding.root
    }
    private fun androidBackButton(){
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Exit the application
                    requireActivity().finishAffinity()
                }
            }
        )
    }
}
