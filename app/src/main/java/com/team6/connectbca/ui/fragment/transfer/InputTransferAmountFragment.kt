package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.team6.connectbca.databinding.FragmentInputTransferAmountBinding
import com.team6.connectbca.domain.model.SavedAccountData
import com.team6.connectbca.ui.fragment.adapter.transfer.InputTransferAmountTabPagerAdapter
import com.team6.connectbca.ui.listener.TransferDataListener
import com.team6.connectbca.ui.viewmodel.InputTransferAmountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InputTransferAmountFragment : Fragment(), TransferDataListener {
    private var beneficiaryId: String? = null
    private var beneficiaryAccountNumber: String? = null
    private var beneficiaryAccountName: String? = null
    private val viewModel by viewModel<InputTransferAmountViewModel>()
    private lateinit var binding: FragmentInputTransferAmountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputTransferAmountBinding.inflate(layoutInflater, container, false)
        arguments?.let {
            beneficiaryId = it.getString("beneficiaryId")
            beneficiaryAccountNumber = it.getString("beneficiaryAccountNumber")
            beneficiaryAccountName = it.getString("beneficiaryAccountName")
        }

        viewModel.getLoading().observe(viewLifecycleOwner) {
            if (it) {
                binding.transferProgressBar.visibility = View.VISIBLE
            } else {
                binding.transferProgressBar.visibility = View.GONE
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayout()
        val navController = findNavController()

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.background = resources.getDrawable(android.R.color.transparent)
        binding.toolbar.title = "Masukan Nominal"

        setData()
    }

    private fun setData() {
        binding.logoText.text = getAcronym(beneficiaryAccountName ?: "Pengguna")
        binding.recipientName.text = beneficiaryAccountName
        binding.recipientBankId.text = beneficiaryAccountNumber
    }

    private fun setupTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = InputTransferAmountTabPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Sekarang" else "Atur Tanggal"
        }.attach()
    }

    private fun getAcronym(word: String): String {
        return if (word.length >= 2) {
            val trimmedString = word.substring(2, word.length)
            word.replace(trimmedString, "").uppercase()
        } else {
            word.uppercase()
        }
    }

    override fun getBeneficiaryData(): SavedAccountData {
        return SavedAccountData(
            savedBeneficiaryId = beneficiaryId,
            beneficiaryAccountNumber = beneficiaryAccountNumber,
            beneficiaryAccountName = beneficiaryAccountName
        )
    }
}