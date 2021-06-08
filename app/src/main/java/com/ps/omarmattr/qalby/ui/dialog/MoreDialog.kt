package com.ps.omarmattr.qalby.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.DialogMoreBinding
import com.ps.omarmattr.qalby.model.MoreItem

class MoreDialog : BottomSheetDialogFragment() ,GenericAdapter.OnListItemViewClickListener<MoreItem>{
    private val moreArray = arrayListOf(
        MoreItem(name = "Qblah", image = 0),
        MoreItem(name = "Quran", image = 0),
        MoreItem(name = "Setting", image = 0)
    )
    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_more,BR.more,this)
    }
    private val mBinding by lazy {
        DialogMoreBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.data = moreArray
        mBinding.rcMore.apply {
            adapter = mAdapter
        }
    }

    override fun onClickItem(itemViewModel: MoreItem, type: Int, position: Int) {
    }
}