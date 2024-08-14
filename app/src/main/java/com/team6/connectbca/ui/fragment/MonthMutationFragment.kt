package com.team6.connectbca.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentMonthBinding
import com.team6.connectbca.domain.model.MonthMutationListItem
import com.team6.connectbca.ui.fragment.adapter.monthmutation.MonthMutationAdapter
import com.team6.connectbca.ui.fragment.adapter.monthmutation.MonthMutationAdapterListener
import com.team6.connectbca.ui.viewmodel.MonthMutationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MonthMutationFragment : Fragment(), MonthMutationAdapterListener {

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<MonthMutationViewModel>()
    private val adapter = MonthMutationAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view.context)
        setData()

        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.monthMutationProgressBar.visibility = View.VISIBLE
            } else {
                binding.monthMutationProgressBar.visibility = View.GONE
                Snackbar.make(binding.root, "Mutasi berhasil dimuat", Snackbar.LENGTH_LONG).show()
            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat data mutasi", Snackbar.LENGTH_LONG).show()
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
        showQuickAccessAlertDialog()
    }

    private fun setupRecyclerView(context: Context) {
        binding.monthMutationRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.monthMutationRecyclerView.adapter = adapter
        binding.monthMutationRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setData() {
        viewModel.getThisMonthMutation().observe(viewLifecycleOwner) { transactionGroup ->
            if (!transactionGroup.isNullOrEmpty()) {
                val newList = mutableListOf<MonthMutationListItem>()

                binding.tvNoMutationMonth.visibility = View.GONE
                transactionGroup.forEach { transaction ->
                    newList.apply {
                        add(transaction.dateTime!!)
                        transaction.transactionGroup?.forEach { mutation ->
                            add(mutation)
                        }
                    }
                }

                adapter.submitList(newList)
            } else {
                binding.tvNoMutationMonth.visibility = View.VISIBLE
            }
        }
    }

    private fun showQuickAccessAlertDialog() {
        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)

        dialog.setContentView(R.layout.item_quick_access_notfound_alert)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val closeBtn: MaterialButton = dialog.findViewById(R.id.quickAccessAlertCloseBtn)
        closeBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}