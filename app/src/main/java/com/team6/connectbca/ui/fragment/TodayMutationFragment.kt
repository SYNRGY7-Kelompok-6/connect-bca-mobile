package com.team6.connectbca.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentTodayBinding
import com.team6.connectbca.ui.fragment.adapter.todaymutation.TodayMutationAdapter
import com.team6.connectbca.ui.fragment.adapter.todaymutation.TodayMutationAdapterListener
import com.team6.connectbca.ui.viewmodel.TodayMutationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TodayMutationFragment : Fragment(), TodayMutationAdapterListener {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<TodayMutationViewModel>()
    private val adapter = TodayMutationAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view.context)
        setData()

//        viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
//            val progressBar = view.findViewById<LinearProgressIndicator>(R.id.mutationProgressBar)
//            if (isLoading) {
//                progressBar.visibility = View.VISIBLE
//            } else {
//                progressBar.visibility = View.GONE
//            }
//        }

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat info saldo", Snackbar.LENGTH_SHORT).show()
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
        binding.todayMutationRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.todayMutationRecyclerView.adapter = adapter
        binding.todayMutationRecyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setData() {
        viewModel.getTodayMutation().observe(viewLifecycleOwner) { mutations ->
            if (!mutations.isNullOrEmpty()) {
                binding.tvNoMutationToday.visibility = View.GONE
                adapter.submitList(mutations)
            } else {
                binding.tvNoMutationToday.visibility = View.VISIBLE
            }
        }
    }
}