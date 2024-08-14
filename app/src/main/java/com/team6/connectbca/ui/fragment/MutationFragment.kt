package com.team6.connectbca.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        val customerBankcardBinding = ItemCustomerBankCardBinding.bind(customerBankcard)
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
                Snackbar.make(binding.root, "Data berhasil dimuat", Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat info saldo", Snackbar.LENGTH_LONG).show()
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
                0 -> "Mutasi Hari Ini"
                1 -> "Mutasi Bulan Ini"
                2 -> "Cari Mutasi Berdasarkan Rentang Tanggal"
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
            screenHeight <= 1385 -> screenHeight * 0.5
            screenHeight <= 1440 -> screenHeight * 0.35
            screenHeight <= 1920 -> screenHeight * 0.4
            else -> screenHeight * 0.45
        }
        bottomSheetBehavior.peekHeight = peekHeight.toInt()
        val maxHeight = (screenHeight * 1.0).toInt()
        bottomSheetBehavior.maxHeight = maxHeight

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        hideInquiryInfo()
                        binding.dragHandle.contentDescription = "Tarik ke bawah untuk keluar dari layar penuh mutasi"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        showInquiryInfo()
                        binding.dragHandle.contentDescription = "Tarik ke atas untuk melihat mutasi dalam layar penuh"
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        bottomSheetBehavior.isFitToContents = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val alpha = 1 - slideOffset
                binding.cardCustomer.root.alpha = alpha
            }
        })
    }


    private fun setData() {
        viewModel.getBalanceInquiry().observe(viewLifecycleOwner) { balanceInquiry ->
            if (balanceInquiry != null) {
                val amount = balanceInquiry.balance?.availableBalance?.value
                val formattedAmount = getFormattedBalance(amount!!)
                val formattedAccNo = getFormattedAccountNo(balanceInquiry.accountNo!!.toDouble())
                val formattedExpDate = miliseondToDateMonth(balanceInquiry.accountCardExp!!.toLong())

                binding.tvBalanceAmount.text = "*********"
                binding.tvBalanceAmount.contentDescription = "Jumlah saldo disembunyikan"
                balance = formattedAmount
                binding.cardCustomer.tvCardNumber.text = formattedAccNo
                binding.cardCustomer.tvCardNumber.contentDescription = formattedAccNo
                binding.cardCustomer.tvSavingProduct.text = balanceInquiry.accountType
                binding.cardCustomer.tvSavingProduct.contentDescription = "Tipe kartu adalah ${balanceInquiry.accountType}"
                binding.cardCustomer.tvExpDate.text = formattedExpDate
                binding.cardCustomer.tvExpDate.contentDescription = formattedExpDate
            }
        }

        viewModel.getAccountMonthly().observe(viewLifecycleOwner) { monthly ->
            if (monthly != null) {
                val deposit = getFormattedBalance(monthly.monthlyIncome?.value!!)
                val withdrawal = getFormattedBalance(monthly.monthlyOutcome?.value!!)

                binding.tvRecentMonthDepositAmount.text = "Rp $deposit"
                binding.tvRecentMonthDepositAmount.contentDescription = "$deposit rupiah"
                binding.tvRecentMonthWithdrawalAmount.text = "Rp $withdrawal"
                binding.tvRecentMonthWithdrawalAmount.contentDescription = "$withdrawal rupiah"
            }
        }

        viewModel.getMonthName().observe(viewLifecycleOwner) { month ->
            binding.tvRecentMonthDeposit.text = month
            binding.tvRecentMonthDeposit.contentDescription = month
            binding.tvRecentMonthWithdrawal.text = month
            binding.tvRecentMonthWithdrawal.contentDescription = month
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboardManager.setPrimaryClip(clip)
    }

    private fun showBalance() {
        isBalanceVisible = !isBalanceVisible

        if (isBalanceVisible) {
            binding.tvBalanceAmount.text = balance
            binding.tvBalanceAmount.contentDescription = balance
            binding.btnShowBalance.setIconResource(R.drawable.ic_visibility_off)
            binding.btnShowBalance.contentDescription = "Klik tombol ini untuk sembunyikan saldo"
        } else {
            binding.tvBalanceAmount.text = "*********"
            binding.tvBalanceAmount.contentDescription = "Jumlah saldo disembunyikan"
            binding.btnShowBalance.setIconResource(R.drawable.ic_visibility)
            binding.btnShowBalance.contentDescription = "Klik tombol ini untuk melihat saldo"
        }
    }

    private fun hideInquiryInfo() {
        binding.tvBalanceText.visibility = View.GONE
        binding.tvBalanceCurrency.visibility = View.GONE
        binding.cardCustomer.root.visibility = View.GONE
        binding.tvBalanceAmount.visibility = View.GONE
        binding.cvDeposit.visibility = View.GONE
        binding.cvWithdrawal.visibility = View.GONE
        binding.btnShowBalance.visibility = View.GONE
    }

    private fun showInquiryInfo() {
        binding.tvBalanceText.visibility = View.VISIBLE
        binding.tvBalanceCurrency.visibility = View.VISIBLE
        binding.cardCustomer.root.visibility = View.VISIBLE
        binding.tvBalanceAmount.visibility = View.VISIBLE
        binding.cvDeposit.visibility = View.VISIBLE
        binding.cvWithdrawal.visibility = View.VISIBLE
        binding.btnShowBalance.visibility = View.VISIBLE
    }
}