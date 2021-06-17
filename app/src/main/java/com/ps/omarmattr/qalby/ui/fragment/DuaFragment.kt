package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.ViewPagerAdapter
import com.ps.omarmattr.qalby.databinding.FragmentDuaBinding
import com.ps.omarmattr.qalby.other.TYPE_SOUND
import com.ps.omarmattr.qalby.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DuaFragment : Fragment(R.layout.fragment_dua), PopupMenu.OnMenuItemClickListener {

    private lateinit var mBinding: FragmentDuaBinding
    private val shared by lazy {
        PreferencesManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentDuaBinding.bind(view)
        initViewPage()

        mBinding.goToSetting.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.menu_dua)
            popup.show()
        }
    }


    private fun initViewPage() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.male -> {
                shared.editor.putInt(TYPE_SOUND, 1).apply()
                return true;
            }
            R.id.female -> {
                shared.editor.putInt(TYPE_SOUND, 2).apply()
                return true;
            }
        }
        return false;
    }


}

