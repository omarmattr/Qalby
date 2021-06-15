package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.adapter.ViewPagerAdapter
import com.ps.omarmattr.qalby.databinding.FragmentBulletinBinding
import com.ps.omarmattr.qalby.databinding.FragmentDuaBinding
import com.ps.omarmattr.qalby.model.Dua
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class DuaFragment : Fragment() {


    private val mBinding by lazy {
        FragmentDuaBinding.inflate(layoutInflater)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initViewPage()
    }

    override fun onResume() {
        super.onResume()
        initViewPage()
        Log.e("tttttttttt","OnResume")
    }

    private fun initViewPage() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        val listDuaFragment = ListDuaFragment()
        viewPagerAdapter.addFragment(listDuaFragment, "Dua")
//        viewPagerAdapter.addFragment(DuaDetailsFragment(), "Saved")
        viewPagerAdapter.addFragment(ListDuaFragment(), "Hifz")
        mBinding.viewPager.adapter = viewPagerAdapter
        mBinding.tableLayout.setupWithViewPager(mBinding.viewPager)
//        TabLayoutMediator(
//            mBinding.tableLayout, mBinding.viewPager
//        ) { tab: TabLayout.Tab, position: Int ->
//            when (position) {
//                0 -> {
//                    tab.text = "Songs"
//                }
//                1 -> {
//                    tab.text = "Album"
//                }
//                2 -> {
//                    tab.text = "Album"
//                }
//            }
//        }.attach()

    }




}

