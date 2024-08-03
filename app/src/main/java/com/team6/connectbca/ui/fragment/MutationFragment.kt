package com.team6.connectbca.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentMutationBinding
import com.team6.connectbca.databinding.ItemCustomerBankCardBinding
import com.team6.connectbca.ui.fragment.adapter.TabPagerAdapter
import com.team6.connectbca.ui.viewmodel.BalanceInquiryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MutationFragment : Fragment() {
    private var _binding: FragmentMutationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<BalanceInquiryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMutationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerBankcard = binding.root.findViewById<CardView>(R.id.cardCustomer)
        val customerBankcardBinding = ItemCustomerBankCardBinding.bind(customerBankcard)
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = "Informasi Rekening"

        setupTabLayout()
        setupBottomSheet()
        setData()

        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            binding.mutationProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, "Gagal memuat info saldo", Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.getSuccess().observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.root, "Sukses memuat info saldo", Snackbar.LENGTH_SHORT).show()
            }
        }

        customerBankcardBinding.iconButtonCopy.setOnClickListener {
            copyToClipboard(customerBankcardBinding.tvCardNumber.text.toString())
        }
    }

    private fun setupTabLayout() {
        val adapter = TabPagerAdapter(parentFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Hari Ini"
                1 -> "Bulan Ini"
                2 -> "Cari"
                else -> null
            }
        }.attach()
    }

    private fun setupBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        val screenHeight = resources.displayMetrics.heightPixels

        val peekHeight = when {
            screenHeight <= 480 -> screenHeight * 0.15
            screenHeight <= 800 -> screenHeight * 0.2
            screenHeight <= 1024 -> screenHeight * 0.25
            screenHeight <= 1280 -> screenHeight * 0.3
            screenHeight <= 1440 -> screenHeight * 0.35
            screenHeight <= 1920 -> screenHeight * 0.4
            else -> screenHeight * 0.45
        }

        bottomSheetBehavior.peekHeight = peekHeight.toInt()
        bottomSheetBehavior.maxHeight = (screenHeight * 1.0).toInt()
    }

    private fun setData() {
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) { balanceInquiry ->
            balanceInquiry?.let { accountInfo ->
                "Rp ${accountInfo.balance}".also { binding.tvBalanceAmount.text = it }
                binding.cardCustomer.tvCardNumber.text = accountInfo.accountNo
                binding.cardCustomer.tvSavingProduct.text = accountInfo.accountType
                binding.cardCustomer.tvExpDate.text = accountInfo.accountCardExp
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = (requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
            setPrimaryClip(ClipData.newPlainText("Copied Text", text))
        }
        showSnackbar("Account Number is copied to clipboard")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}