package com.ps.omarmattr.qalby.ui.dialog

import com.ps.omarmattr.qalby.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.FragmentSolahSettingBinding
import com.ps.omarmattr.qalby.model.Setting
import com.ps.omarmattr.qalby.other.PREFERENCES_ADDRESS_NAME
import com.ps.omarmattr.qalby.other.PREFERENCES_METHOD_NAME
import com.ps.omarmattr.qalby.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingSolahFragment : BottomSheetDialogFragment() ,GenericAdapter.OnListItemViewClickListener<Setting>{
    private val mBinding by lazy {
        FragmentSolahSettingBinding.inflate(layoutInflater)
    }
    private lateinit var preferencesManager: PreferencesManager

    private val arraySetting by lazy {   arrayListOf(
        Setting(0, getString(R.string.location), ""),
        Setting(1, getString(R.string.muezzin), ""),
        Setting(2, getString(R.string.calculation), ""),
    )}
    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_solah_setting,BR.setting,this)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels ).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        dialog!!.window?.setLayout(width, height)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())

        preferencesManager.sharedPreferences.getString(
            PREFERENCES_ADDRESS_NAME,
            ""
        )?.let {
            arraySetting[0].subTitle = it
        }
        preferencesManager.sharedPreferences.getString(
            PREFERENCES_METHOD_NAME,
            ""
        )?.let {
            arraySetting[2].subTitle = it
        }
        mBinding.apply {
            backBtn.setOnClickListener {
               dismiss()
            }
            mAdapter.data = arraySetting
            rcSetting.adapter = mAdapter

        }

    }

    override fun onClickItem(itemViewModel: Setting, type: Int, position: Int) {
        when(itemViewModel.id){
            0->LocationDialog().show(childFragmentManager,"")
            1->MuezzinDialog().show(childFragmentManager,"")
            2->CalculationDialog().show(childFragmentManager,"")
        }
    }
}