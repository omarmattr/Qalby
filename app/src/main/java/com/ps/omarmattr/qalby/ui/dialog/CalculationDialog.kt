package com.ps.omarmattr.qalby.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ps.omarmattr.qalby.databinding.DialogCalcualateBinding
import com.ps.omarmattr.qalby.other.PREFERENCES_METHOD
import com.ps.omarmattr.qalby.other.PREFERENCES_METHOD_NAME
import com.ps.omarmattr.qalby.util.PreferencesManager

class CalculationDialog : BottomSheetDialogFragment() {
    private val mBinding by lazy {
        DialogCalcualateBinding.inflate(layoutInflater)
    }
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())
        mBinding.apply {
            preferencesManager.sharedPreferences.getInt(
                PREFERENCES_METHOD,
                3
            ).let {
                when (it) {
                    0 -> method1.isChecked = true
                    1 -> method1.isChecked = true
                    2 -> method2.isChecked = true
                    3 -> method3.isChecked = true
                    4 -> method4.isChecked = true
                    5 -> method4.isChecked = true
                    7 -> method7.isChecked = true
                    8 -> method8.isChecked = true
                    9 -> method9.isChecked = true
                    10 -> method10.isChecked = true
                    11 -> method11.isChecked = true
                    12 -> method12.isChecked = true
                    13 -> method13.isChecked = true
                    14 -> method13.isChecked = true
                }
            }
            rgMethod.setOnCheckedChangeListener { v, checkedId ->
                val editor = preferencesManager.editor
                when (checkedId) {
                    method0.id -> setPreferences(0)
                    method1.id -> setPreferences(1)
                    method2.id -> setPreferences(2)
                    method3.id -> setPreferences(3)
                    method4.id -> setPreferences(4)
                    method5.id -> setPreferences(5)
                    method7.id -> setPreferences(7)
                    method8.id -> setPreferences(8)
                    method9.id -> setPreferences(9)
                    method10.id -> setPreferences(10)
                    method11.id -> setPreferences(11)
                    method12.id -> setPreferences(12)
                    method13.id -> setPreferences(13)
                    method1.id -> setPreferences(14)
                }
                val name = v.findViewById<RadioButton>(checkedId).text.toString()
                editor.putString(PREFERENCES_METHOD_NAME, name)
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        preferencesManager.editor.apply()
    }
    private fun setPreferences(method: Int) {
        preferencesManager.editor.putInt(PREFERENCES_METHOD, method)
    }
}