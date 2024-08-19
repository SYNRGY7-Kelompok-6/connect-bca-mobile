package com.team6.connectbca.ui.fragment.mutation

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentSearchBinding
import com.team6.connectbca.domain.model.MonthMutationListItem
import com.team6.connectbca.extensions.getYear
import com.team6.connectbca.ui.fragment.adapter.searchmutation.SearchMutationAdapter
import com.team6.connectbca.ui.fragment.adapter.searchmutation.SearchMutationAdapterListener
import com.team6.connectbca.ui.viewmodel.SearchMutationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchMutationFragment : Fragment(), SearchMutationAdapterListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchMutationViewModel>()
    private val adapter = SearchMutationAdapter(this)

    private var startDate: String = ""
    private var endDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view.context)

        binding.etStartDate.setOnClickListener {
            showCustomDatePicker(true, view.context)
        }

        binding.etEndDate.setOnClickListener {
            showCustomDatePicker(false, view.context)
        }

        binding.cbUseSameDate.setOnClickListener { isSameDateChecked() }

        binding.btnSearchMutation.setOnClickListener {
            endDate = binding.etEndDate.text.toString()

            if (checkDateInput()) {
                setData(view.context)
            }
        }

        binding.btnSearchAgain.setOnClickListener {
            binding.flipperSearchMutation.displayedChild = 0
        }

        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.searchMutationProgressBar.visibility = View.VISIBLE
                binding.flipperSearchMutation.displayedChild = 0
            } else {
                binding.searchMutationProgressBar.visibility = View.GONE
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat data mutasi", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickSeeInvoice(transactionId: String) {
        navigateToPaymentReceipt(transactionId)
    }

    private fun setupRecyclerView(context: Context) {
        binding.searchMutationRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.searchMutationRecyclerView.adapter = adapter
        binding.searchMutationRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setData(context: Context) {
        viewModel.getSearchedMutation(startDate, endDate).observe(viewLifecycleOwner) { transactionGroup ->
            if (!transactionGroup.isNullOrEmpty()) {
                val newList = mutableListOf<MonthMutationListItem>()

                transactionGroup.forEach { transaction ->
                    newList.apply {
                        add(transaction.dateTime!!)
                        transaction.transactionGroup?.forEach { mutation ->
                            add(mutation)
                        }
                    }
                }

                adapter.submitList(newList)

                binding.flipperSearchMutation.displayedChild = 1
                Snackbar.make(binding.root, "Berhasil mendapatkan transaksi", Snackbar.LENGTH_SHORT).show()
            } else {
                binding.flipperSearchMutation.displayedChild = 0
                Snackbar.make(binding.root, "Belum ada transaksi", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun isSameDateChecked() {
        if (binding.cbUseSameDate.isChecked) {
            binding.etEndDate.setText(startDate)
        } else {
            binding.etEndDate.text?.clear()
        }
    }

    private fun checkDateInput() : Boolean {
        val startMonth = startDate.substring(3,5)
        val startYear = startDate.substring(6)
        val endMonth = endDate.substring(3,5)
        val endYear = endDate.substring(6)

        if (startDate > endDate &&
            (startMonth.compareTo(endMonth) >= 0 && startYear.compareTo(endYear) >= 0)) {
            binding.etStartDate.error = "Tanggal mulai lebih besar dari tanggal akhir"
            binding.etEndDate.error = "Tanggal akhir lebih kecil dari tanggal mulai"

            return false
        } else {
            binding.etStartDate.error = null
            binding.etEndDate.error = null

            return true
        }
    }

    private fun showCustomDatePicker(isStart: Boolean, context: Context) {
        val dialog = Dialog(context)
        var day = ""
        var month = ""
        var year = ""

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_custom_datepicker)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val saveBtn: MaterialButton = dialog.findViewById(R.id.getDateBtn)
        val dayNumPick: NumberPicker? = dialog.findViewById(R.id.dayNumPick)
        val monthNumPick: NumberPicker? = dialog.findViewById(R.id.monthNumPick)
        val yearNumPick: NumberPicker? = dialog.findViewById(R.id.yearNumPick)
        val nowYear = getYear().toInt()
        val monthVals = arrayOf<String>("Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember")

        dayNumPick?.minValue = 1
        dayNumPick?.maxValue = 31
        monthNumPick?.minValue = 1
        monthNumPick?.maxValue = 12
        monthNumPick?.displayedValues = monthVals
        yearNumPick?.minValue = nowYear - 5
        yearNumPick?.maxValue = nowYear

        saveBtn.setOnClickListener {
            day = dayNumPick?.value.toString()
            month = monthNumPick?.value.toString()
            year = yearNumPick?.value.toString()

            if (isStart) {
                startDate = "$day-$month-$year"
                binding.etStartDate.setText(startDate)
            } else {
                endDate = "$day-$month-$year"
                binding.etEndDate.setText(endDate)
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun navigateToPaymentReceipt(transactionId: String) {
        val action = MutationFragmentDirections.actionMutationFragmentToPaymentReceiptFragment(transactionId, true)
        findNavController().navigate(action)
    }
}