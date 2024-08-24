package com.team6.connectbca.ui.fragment

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentHomeBinding
import com.team6.connectbca.extensions.getFormattedAccountNo
import com.team6.connectbca.extensions.getFormattedBalance
import com.team6.connectbca.ui.activity.LoginActivity
import com.team6.connectbca.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModel<HomeViewModel>()
    private var isBalanceVisible = false
    private var balance = "1234567890"
    private var balanceDesc = "balance"

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
        setupCustomerInfo()
        androidBackButton()
        
        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.homeProgressBar.visibility = View.VISIBLE
            } else {
                binding.homeProgressBar.visibility = View.GONE
            }
        }

        viewModel.getSuccess().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess != null) {
                navigateToLogin()
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                showSnackbar("Gagal memuat data rekening")
            }
        }

        binding.ivLogoutIcon.setOnClickListener {
            showConfirmationDialog()
        }

        binding.homeQrisIcon.setOnClickListener {
            navigateToQRIS()
        }
        binding.homeMutationIcon.setOnClickListener {
            navigateToMutation()
        }

        binding.homeTransferIcon.setOnClickListener {
            navigateToTransfer()
        }

        binding.btnIconCopy.setOnClickListener {
            copyToClipboard(accountNumber)
        }

        binding.btnIconVisible.setOnClickListener {
            iconBalanceVisibility()
        }

        binding.homeEwalletIcon.setOnClickListener { showAlertDialog() }
        binding.homeBillsIcon.setOnClickListener { showAlertDialog() }
        binding.homeInvestationIcon.setOnClickListener { showAlertDialog() }
        binding.newsItem1.btnSeeNews.setOnClickListener { showAlertDialog() }
        binding.newsItem2.btnSeeNews.setOnClickListener { showAlertDialog() }
    }

    private fun setupCustomerInfo() {
        viewModel.getAccountInfo().observe(viewLifecycleOwner) { account ->
            if (account != null) {
                val amount = account.balance?.availableBalance?.value
                val formattedAmount = getFormattedBalance(amount!!)
                val formattedAccNo = getFormattedAccountNo(account.accountNo!!.toDouble())

                binding.salutationText.setText("Hi, ${account.name}")
                binding.tvAccountNumber.text = formattedAccNo
                binding.tvAccountNumber.contentDescription =
                    account.accountNo?.split("")!!.joinToString(" ")
                balance = formattedAmount
                balanceDesc = amount.toString()
            }
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

    private fun navigateToTransfer() {
        val action = HomeFragmentDirections.actionHomeFragmentToTransferFragment()
        findNavController().navigate(action)
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clip)
    }

    private fun iconBalanceVisibility() {
        isBalanceVisible = !isBalanceVisible
        if (isBalanceVisible) {
            binding.tvBalance.text = balance
            binding.tvBalance.contentDescription = balanceDesc
            binding.btnIconVisible.setIconResource(R.drawable.ic_visibility_off)
            binding.btnIconVisible.contentDescription = "Klik tombol ini untuk sembunyikan saldo"
        } else {
            binding.tvBalance.text = "***********"
            binding.tvBalance.contentDescription = "Jumlah saldo disembunyikan"
            binding.btnIconVisible.setIconResource(R.drawable.ic_visibility)
            binding.btnIconVisible.contentDescription = "Klik tombol ini untuk melihat saldo"
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apa Anda yakin ingin keluar dari aplikasi ini?")
            .setPositiveButton("Ya") { dialog, id ->
                viewModel.userLogout()
            }
            .setNegativeButton("Tidak") { dialog, id ->
                dialog.dismiss()
            }
            .setCancelable(true)

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.dialogButtonColor))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.dialogButtonColor))
    }

    private fun showAlertDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_quick_access_notfound_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeBtn: MaterialButton = dialog.findViewById(R.id.quickAccessAlertCloseBtn)
        val alertMessage: TextView = dialog.findViewById(R.id.quickAccessAlertMessage)

        closeBtn.setOnClickListener { dialog.dismiss() }
        alertMessage.setText(R.string.feature_not_found)

        dialog.show()
    }

    private fun navigateToLogin() {
        LoginActivity.startActivity(requireContext())
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

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}