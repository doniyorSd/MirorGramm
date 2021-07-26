package com.example.mirrorgram.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mirrorgram.GroupFragment
import com.example.mirrorgram.UserFragment
import com.example.mirrorgram.models.User

class ViewPagerAdapter(private var fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    var fm = UserFragment()
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return if (position == 0) {
            fm
        } else {
            GroupFragment()
        }
    }
}