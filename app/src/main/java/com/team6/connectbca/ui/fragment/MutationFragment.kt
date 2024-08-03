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
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.R
import com.team6.connectbca.databinding.CustomerBankCardBinding
import com.team6.connectbca.databinding.FragmentMutationBinding
import com.team6.connectbca.extensions.getFormattedAccountNo
import com.team6.connectbca.extensions.getFormattedBalance
import com.team6.connectbca.extensions.miliseondToDateMonth
import com.team6.connectbca.ui.fragment.adapter.TabPagerAdapter
import com.team6.connectbca.ui.viewmodel.BalanceInquiryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MutationFragment : Fragment() {
    private lateinit var binding: FragmentMutationBinding
    private lateinit var balance: String
    private val viewModel by viewModel<BalanceInquiryViewModel>()
    private var isBalanceVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMutationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerBankcard = binding.root.findViewById<CardView>(R.id.cardCustomer)
        val customerBankcardBinding = CustomerBankCardBinding.bind(customerBankcard)
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.setTitle("Informasi Saldo")

        setupTabLayout()
        setupBottomSheet()
        setData()

        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.mutationProgressBar.visibility = View.VISIBLE
            } else {
                binding.mutationProgressBar.visibility = View.GONE
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat info saldo", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnShowBalance.setOnClickListener { showBalance() }

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
            tab.contentDescription = when (position) {
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

        val maxHeight = (screenHeight * 1.0).toInt()
        bottomSheetBehavior.maxHeight = maxHeight
    }

    private fun setData() {
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) { balanceInquiry ->
            if (balanceInquiry != null) {
                val amount = balanceInquiry.balance?.availableBalance?.value
                val formattedAmount = getFormattedBalance(amount!!)
                val formattedAccNo = getFormattedAccountNo(balanceInquiry.accountNo!!.toDouble())
                val formattedExpDate = miliseondToDateMonth(balanceInquiry.accountCardExp!!.toLong())

                binding.tvBalanceAmount.text = "*********"
                balance = formattedAmount
                binding.cardCustomer.tvCardNumber.text = formattedAccNo
                binding.cardCustomer.tvSavingProduct.text = balanceInquiry.accountType
                binding.cardCustomer.tvExpDate.text = formattedExpDate
            }
        }

        viewModel.getAccountMonthly().observe(viewLifecycleOwner) { monthly ->
            if (monthly != null) {
                val deposit = getFormattedBalance(monthly.monthlyIncome?.value!!)
                val withdrawal = getFormattedBalance(monthly.monthlyOutcome?.value!!)

                binding.tvRecentMonthDepositAmount.text = "Rp $deposit"
                binding.tvRecentMonthWithdrawalAmount.text = "Rp $withdrawal"
            }
        }

        viewModel.getMonthName().observe(viewLifecycleOwner) { month ->
            binding.tvRecentMonthDeposit.text = month
            binding.tvRecentMonthWithdrawal.text = month
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = (requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
            setPrimaryClip(ClipData.newPlainText("Copied Text", text))
        }
        showSnackbar("Nomor rekening berhasil disalin")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showBalance() {
        isBalanceVisible = !isBalanceVisible

        if (isBalanceVisible) {
            binding.tvBalanceAmount.text = balance
            binding.btnShowBalance.setIconResource(R.drawable.ic_visibility_off)
        } else {
            binding.tvBalanceAmount.text = "*********"
            binding.btnShowBalance.setIconResource(R.drawable.ic_visibility)
        }
    }
}