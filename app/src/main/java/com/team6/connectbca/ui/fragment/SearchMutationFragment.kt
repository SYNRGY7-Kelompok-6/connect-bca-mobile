package com.team6.connectbca.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentSearchBinding
import com.team6.connectbca.domain.model.MonthMutationListItem
import com.team6.connectbca.extensions.reformatDate
import com.team6.connectbca.ui.fragment.adapter.searchmutation.SearchMutationAdapter
import com.team6.connectbca.ui.fragment.adapter.searchmutation.SearchMutationAdapterListener
import com.team6.connectbca.ui.viewmodel.SearchMutationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchMutationFragment : Fragment(), SearchMutationAdapterListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchMutationViewModel>()
    private val adapter = SearchMutationAdapter(this)

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

        var startDate: String = ""

        binding.etStartDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih tanggal")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTheme(R.style.ThemeOverlay_App_DatePicker)
                    .build()

            datePicker.show(parentFragmentManager, "Show Date Picker")

            datePicker.addOnPositiveButtonClickListener {
                startDate = reformatDate(
                    datePicker.headerText.toString(),
                    "MMM d, yyyy",
                    "dd-MM-yyyy"
                )
                binding.etStartDate.setText(startDate)
            }
        }

        binding.etEndDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih tanggal")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTheme(R.style.ThemeOverlay_App_DatePicker)
                    .build()

            datePicker.show(parentFragmentManager, "Show Date Picker")

            datePicker.addOnPositiveButtonClickListener {
                binding.etEndDate.setText(
                    reformatDate(
                        datePicker.headerText.toString(),
                        "MMM d, yyyy",
                        "dd-MM-yyyy"
                    )
                )
            }
        }

        binding.cbUseSameDate.setOnClickListener { binding.etEndDate.setText(startDate) }

        binding.btnSearchMutation.setOnClickListener {
            val endDate = binding.etEndDate.text.toString()

            setData(startDate, endDate, view.context)
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

    override fun onClickSeeInvoice() {
        TODO("Not yet implemented")
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

    private fun setData(startDate: String, endDate: String, context: Context) {
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
}