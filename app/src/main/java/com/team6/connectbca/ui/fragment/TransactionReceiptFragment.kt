package com.team6.connectbca.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.team6.connectbca.databinding.FragmentTransactionReceiptBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionReceiptFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionReceiptFragment : Fragment() {

    private val binding by lazy { FragmentTransactionReceiptBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Informasi Rekening"
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }
}