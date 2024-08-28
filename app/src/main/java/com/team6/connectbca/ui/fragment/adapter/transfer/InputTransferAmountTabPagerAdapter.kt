package com.team6.connectbca.ui.fragment.adapter.transfer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team6.connectbca.ui.fragment.transfer.InputScheduledTransferAmountFragment
import com.team6.connectbca.ui.fragment.transfer.InputTransferNowAmountFragment

class InputTransferAmountTabPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InputTransferNowAmountFragment()
            1 -> InputScheduledTransferAmountFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}