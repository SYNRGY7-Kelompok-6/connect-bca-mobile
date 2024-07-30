package com.team6.connectbca.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var isBalanceVisible = false
    private val balance = "1234567890"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val accountNumber = binding.tvAccountNumber.text.toString()

        binding.homeQrisIcon.setOnClickListener {
            navigateToQRIS()
        }
        binding.homeMutationIcon.setOnClickListener {
            navigateToMutation()
        }

        binding.btnIconCopy.setOnClickListener {
            copyToClipboard(accountNumber)
        }

        binding.btnIconVisible.setOnClickListener {
            iconBalanceVisibility()
        }
    }

    private fun navigateToQRIS() {
        val action = HomeFragmentDirections.actionHomeFragmentToQrisFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMutation() {
        val action = HomeFragmentDirections.actionHomeFragmentToMutationFragment()
        findNavController().navigate(action)
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clip)
        showToast("Text copied to clipboard")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun iconBalanceVisibility() {
        isBalanceVisible = !isBalanceVisible
        if (isBalanceVisible) {
            binding.tvBalance.text = balance
            binding.btnIconVisible.setIconResource(R.drawable.ic_visibility)
        } else {
            binding.tvBalance.text = "***********"
            binding.btnIconVisible.setIconResource(R.drawable.ic_visibility_off)
        }
    }
}