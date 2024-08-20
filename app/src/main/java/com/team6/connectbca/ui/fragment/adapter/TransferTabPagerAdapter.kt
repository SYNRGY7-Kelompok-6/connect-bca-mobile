package com.team6.connectbca.ui.fragment.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team6.connectbca.ui.fragment.transfer.FavoritesTransferFragment
import com.team6.connectbca.ui.fragment.transfer.ScheduledTransfersFragment

class TransferTabPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoritesTransferFragment()
            1 -> ScheduledTransfersFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}