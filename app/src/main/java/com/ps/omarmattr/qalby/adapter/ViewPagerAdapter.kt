package com.ps.omarmattr.qalby.adapter

import android.app.FragmentManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import javax.annotation.Nullable


class ViewPagerAdapter(fragment: FragmentActivity)
    : FragmentStateAdapter(fragment) {

    private val lf = ArrayList<Fragment>()
    private val lt = ArrayList<String>()


    override fun getItemCount(): Int {
        return lf.size
    }

    override fun createFragment(position: Int): Fragment {
        return lf[position]
    }

    fun addFragment(fragment: Fragment?, title: String?) {
        lf.add(fragment!!)
        lt.add(title!!)
    }




}


//class ViewPagerAdapter( fm: androidx.fragment.app.FragmentManager) :
//    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//    var lf: ArrayList<Fragment> = ArrayList()
//    var lt: ArrayList<String> = ArrayList()
//
//    fun addFragment(fragment: Fragment, title: String) {
//        lf.add(fragment)
//        lt.add(title)
//    }
//
//    override fun getCount(): Int {
//        return lf.size
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return lf[position]
//    }
//
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return lt[position]
//    }
//}