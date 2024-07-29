package com.team6.connectbca.ui.fragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team6.connectbca.ui.fragment.MonthMutationFragment
import com.team6.connectbca.ui.fragment.SearchMutationFragment
import com.team6.connectbca.ui.fragment.TodayMutationFragment

class TabPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodayMutationFragment()
            1 -> MonthMutationFragment()
            2 -> SearchMutationFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}