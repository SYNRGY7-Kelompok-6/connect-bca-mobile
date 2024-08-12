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

        viewModel.getError().observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Snackbar.make(binding.root, "Gagal memuat info saldo", Snackbar.LENGTH_SHORT).show()
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