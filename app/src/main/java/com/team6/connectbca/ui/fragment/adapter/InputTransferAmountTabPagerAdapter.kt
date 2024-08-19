package com.team6.connectbca.ui.fragment.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team6.connectbca.ui.fragment.transfer.InputScheduledTransferAmountFragment
import com.team6.connectbca.ui.fragment.transfer.InputTransferAmountFragment

class InputTransferAmountTabPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InputTransferAmountFragment()
            1 -> InputScheduledTransferAmountFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}