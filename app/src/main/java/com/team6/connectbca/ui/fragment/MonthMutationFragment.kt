package com.team6.connectbca.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentMonthBinding
import com.team6.connectbca.domain.model.MutationsItem
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
        setData(view.context)

        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
//            val progressBar = view.findViewById<LinearProgressIndicator>(R.id.mutationProgressBar)
//            if (isLoading) {
//                progressBar.visibility = View.VISIBLE
//            } else {
//                progressBar.visibility = View.GONE
//            }
        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat data mutasi", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickSeeInvoice() {
        TODO("Not yet implemented")
    }

    private fun setupRecyclerView(context: Context) {
        binding.monthMutationRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.monthMutationRecyclerView.adapter = adapter
        binding.monthMutationRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setData(context: Context) {
        viewModel.getThisMonthMutation().observe(viewLifecycleOwner) { transactionGroup ->
            var count = 0

            if (!transactionGroup.isNullOrEmpty()) {
                Snackbar.make(binding.root, "BERHASILLL 2", Snackbar.LENGTH_SHORT).show()
                binding.tvNoMutationMonth.visibility = View.GONE
                transactionGroup.forEach { transaction ->
                    count+=1

                    if (count == 1) {
//                        binding.tvMonth.text = transaction.dateTime
                        adapter.submitList(transaction.transactionGroup)
                    } else {
                        addNewDailyMutationList(context, transaction.dateTime!!, transaction.transactionGroup!!)
                    }
                }
            } else {
                binding.tvNoMutationMonth.visibility = View.VISIBLE
            }
        }
    }

    private fun addNewDailyMutationList(context: Context, text: String, transactions: List<MutationsItem>) {
        val newLayout: ConstraintLayout = view?.findViewById(R.id.dailyMutationList) as ConstraintLayout
        val date = newLayout.findViewById<MaterialTextView>(R.id.tvMonth)
        val list = newLayout.findViewById<RecyclerView>(R.id.monthMutationRecyclerView)

        date.text = text
        list.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        list.adapter = adapter
        list.itemAnimator = DefaultItemAnimator()
        adapter.submitList(transactions)

        binding.root.addView(newLayout)
    }
}