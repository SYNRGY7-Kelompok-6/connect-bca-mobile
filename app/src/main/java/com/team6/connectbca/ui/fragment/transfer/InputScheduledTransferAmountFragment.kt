package com.team6.connectbca.ui.fragment.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.team6.connectbca.databinding.FragmentInputScheduledTransferAmountBinding
import com.team6.connectbca.ui.fragment.adapter.SpinnerAdapter

class InputScheduledTransferAmountFragment : Fragment() {
    private var _binding: FragmentInputScheduledTransferAmountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputScheduledTransferAmountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SpinnerAdapter(
            context = requireContext(),
            transferFrequencySpinner = binding.transferFrequencySpinner,
            scheduleTransferSpinner = binding.scheduleTransferSpinner,
            startDateSpinner = binding.startDateSpinner,
            endDateSpinner = binding.endDateSpinner
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
