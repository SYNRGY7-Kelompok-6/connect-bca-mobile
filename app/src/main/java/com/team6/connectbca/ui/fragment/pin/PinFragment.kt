package com.team6.connectbca.ui.fragment.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textview.MaterialTextView
import com.team6.connectbca.R
import com.team6.connectbca.databinding.FragmentPinBinding
import com.team6.connectbca.ui.viewmodel.PinViewModel

class PinFragment : Fragment() {

    private var _binding: FragmentPinBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinDigits: MutableList<MaterialTextView>
    private val viewModel: PinViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinDigits = mutableListOf(
            binding.circle1,
            binding.circle2,
            binding.circle3,
            binding.circle4,
            binding.circle5,
            binding.circle6
        )

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

        binding.btnDelete.setOnClickListener {
            viewModel.removeDigit()
        }

        viewModel.pinLength.observe(viewLifecycleOwner) { length ->
            updatePinCircles(length)
        }

        viewModel.pinError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                binding.errorMessage.visibility = View.VISIBLE
            } else {
                binding.errorMessage.visibility = View.GONE
            }
        }
    }

    private fun updatePinCircles(length: Int) {
        for (i in 0 until 6) {
            if (i < length) {
                pinDigits[i].text = "*"
                pinDigits[i].setBackgroundResource(R.drawable.filled_circle_background)
            } else {
                pinDigits[i].text = ""
                pinDigits[i].setBackgroundResource(R.drawable.empty_circle_background)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
