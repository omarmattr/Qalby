package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.FragmentListDuaBinding
import com.ps.omarmattr.qalby.model.Dua
import com.ps.omarmattr.qalby.model.getListDua
import com.ps.omarmattr.qalby.ui.viewmodel.DuaViewModel
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ListDuaFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<Dua> {

    @Inject
    lateinit var viewModel: DuaViewModel

    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_dua, BR.dua, this)
    }
    private val mBinding by lazy {
        FragmentListDuaBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter.data = getListDua()
        mBinding.rcData.adapter = mAdapter


    }

    override fun onClickItem(itemViewModel: Dua, type: Int, position: Int) {
        viewModel.getDua(itemViewModel.id)
        findNavController().navigate(R.id.action_destination_dua_to_duaDetailsFragment)

    }
}