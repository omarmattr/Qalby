package com.ps.omarmattr.qalby.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ps.omarmattr.qalby.databinding.DialogMoreBinding

class MoreDialog:BottomSheetDialogFragment() {
    private val mBinding by lazy{
        DialogMoreBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= mBinding.root
}