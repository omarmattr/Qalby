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
class DuaFragment : Fragment(R.layout.fragment_dua) {

    private lateinit var mBinding: FragmentDuaBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentDuaBinding.bind(view)
        initViewPage()
    }


    private fun initViewPage() {
        val viewPagerAdapter =ViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(ListDuaFragment(), "Dua")

        viewPagerAdapter.addFragment(ListDuaFragment(), "Hifz")
        mBinding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(
            mBinding.tableLayout, mBinding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> {
                    tab.text = "Dua"
                }
                1 -> {
                    tab.text = "Saved"
                }
                2 -> {
                    tab.text = "Hifz"
                }
            }
        }.attach()

    }


}

