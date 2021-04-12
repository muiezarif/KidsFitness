package com.muiezarif.kidsfitness.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class FragmentPageAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
//    private val mFragmentTitleList: MutableList<String> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }
    override fun getItemPosition(`object`: Any): Int {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return PagerAdapter.POSITION_NONE
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
//        mFragmentTitleList.add(title)
    }

    fun addFragment(fragment: Fragment, index: Int, title: String) {
        mFragmentList.add(index, fragment)
//        mFragmentTitleList.add(index, title)
    }

    fun removeFragment(index: Int) {
        mFragmentList.removeAt(index)
//        mFragmentTitleList.removeAt(index)
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return mFragmentTitleList.get(position)
//    }
}
