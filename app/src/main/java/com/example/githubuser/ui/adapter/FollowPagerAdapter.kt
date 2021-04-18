package com.example.githubuser.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuser.ui.follow.FollowFragment

class FollowPagerAdapter(
    fragment: Fragment,
    val userLogin: String
) :
    FragmentStateAdapter(fragment) {

    //put val userLogin on constructor os userDetailFragment can pass userLogin to pagerAdapter

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position, userLogin)
    }

    override fun getItemCount(): Int = 2
}