package com.ps.omarmattr.qalby.ui.dialog

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.DialogMuezzinBinding
import com.ps.omarmattr.qalby.model.Azan
import com.ps.omarmattr.qalby.other.FunctionConstant
import com.ps.omarmattr.qalby.other.PREFERENCES_DEFAULT_AZAN
import com.ps.omarmattr.qalby.ui.viewmodel.SolahViewModel
import com.ps.omarmattr.qalby.util.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MuezzinDialog : BottomSheetDialogFragment(),
    GenericAdapter.OnListItemViewClickListener<Azan> {
    private val mBinding by lazy {
        DialogMuezzinBinding.inflate(layoutInflater)
    }
    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_azan, BR.azan, this)
    }

    @Inject
    lateinit var viewModel: SolahViewModel

    lateinit var mediaPlayer: MediaPlayer
    lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferencesManager = PreferencesManager(requireContext())
        mediaPlayer = MediaPlayer.create(context, R.raw.daoodsubuhlong)
        mAdapter.data = FunctionConstant.getAzanList()
        mAdapter.notifyDataSetChanged()
        mBinding.rcAzan.adapter = mAdapter


    }

    override fun onClickItem(itemViewModel: Azan, type: Int, position: Int) {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(context, itemViewModel.media)
        mediaPlayer.start()
        preferencesManager.editor.putInt(PREFERENCES_DEFAULT_AZAN, itemViewModel.media)


    }

    override fun onPause() {
        preferencesManager.editor.apply()
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        super.onPause()
    }

}