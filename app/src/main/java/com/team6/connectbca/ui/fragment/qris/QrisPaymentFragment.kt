package com.team6.connectbca.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentQrisPaymentBinding
import com.team6.connectbca.ui.viewmodel.QrisPaymentViewModel
import java.text.NumberFormat
import java.util.Locale

class QrisPaymentFragment : Fragment() {

    private lateinit var binding: FragmentQrisPaymentBinding
    private val viewModel: QrisPaymentViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQrisPaymentBinding.inflate(inflater, container, false)
        navigateBack()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.addDigit(button.text.toString())
            }
        }
        binding.tvTotalAmount.text = "0"
        binding.btnDelete.setOnClickListener {
            viewModel.removeDigit()
        }
        viewModel.amount.observe(viewLifecycleOwner) {
            val number = it.toDoubleOrNull() ?: 0.0
            val numberFormat = NumberFormat.getNumberInstance(Locale("id", "ID"))
            numberFormat.minimumFractionDigits = 0
            numberFormat.maximumFractionDigits = 0
            val formattedAmount = numberFormat.format(number)
            binding.tvTotalAmount.text = formattedAmount
            binding.tvTotalAmount.contentDescription = "$it rupiah"
        }
    }

    private fun navigateBack() {
        val navController = findNavController()
        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = ""
        binding.toolbar.navigationContentDescription =
            getString(R.string.back_to_menu_button_description)
    }


}