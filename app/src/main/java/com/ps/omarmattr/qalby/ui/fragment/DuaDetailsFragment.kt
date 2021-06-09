package com.ps.omarmattr.qalby.ui.fragment

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.FragmentDuaDetailsBinding
import com.ps.omarmattr.qalby.exoplayer.MusicServiceConnection
import com.ps.omarmattr.qalby.exoplayer.toDua
import com.ps.omarmattr.qalby.model.dua.DuaRequestItem
import com.ps.omarmattr.qalby.ui.viewmodel.DuaViewModel
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DuaDetailsFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<DuaRequestItem> {

    private val mBinding by lazy {
        FragmentDuaDetailsBinding.inflate(layoutInflater)
    }

    val mAdapter by lazy {
        GenericAdapter(R.layout.item_dua_details, BR.duaRequest, this)
    }


    @Inject
    lateinit var viewModel: DuaViewModel

    @Inject
    lateinit var glide: RequestManager


    private var curPlayingSong: DuaRequestItem? = null

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekbar = true

    private var connection: MusicServiceConnection? = null

    var dataList: List<DuaRequestItem>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mBinding.btnPlay.setOnClickListener {
            curPlayingSong?.let {
                viewModel.playOrToggleDua(it, connection!!, true)
            }
        }


        mBinding.seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
//                    mainViewModel.seekTo(it.progress.toLong())
                    shouldUpdateSeekbar = true
                }
            }
        })

        mBinding.viewPager.apply {
            adapter = mAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mBinding.size.text = "${position + 1}/${mAdapter.data.size}"

//                    if (curPlayingSong == null && dataList != null ) {
                        curPlayingSong = dataList!![position]
                        updateTitleAndSongImage(curPlayingSong!!)
                        Log.e("Ttttttttt", "dasdfasdfasdf")
//                    }

                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }
            })
        }


        lifecycleScope.launchWhenStarted {
            viewModel.listDuaStateFlow.collect {
                when (it.status) {
                    ResultRequest.Status.EMPTY -> {
                    }
                    ResultRequest.Status.SUCCESS -> {
                        val data = it.data
                        dataList = data as List<DuaRequestItem>
                        mAdapter.data = dataList!!
                        mBinding.size.text = "1/${data.size}"
                        mAdapter.notifyDataSetChanged()
                        connection = MusicServiceConnection(requireContext())
//                        viewModel.musicServiceConnection = connection
                        viewModel.curPlayingDua = connection!!.curPlayingSong
                        viewModel.playbackState = connection!!.playbackState
                        viewModel.updateCurrentPlayerPosition()
                        subscribeToObservers()
                    }
                    ResultRequest.Status.LOADING -> {
                    }
                    ResultRequest.Status.ERROR -> {
                    }
                }
            }
        }
    }

    override fun onClickItem(itemViewModel: DuaRequestItem, type: Int, position: Int) {
    }

    private fun updateTitleAndSongImage(dua: DuaRequestItem) {
        val title = "${dua.name} - ${dua.ayat}"
    }


    private fun subscribeToObservers() {
//        viewModel.mediaItems.observe(viewLifecycleOwner) {
//            it?.let { result ->
//                when(result.status) {
//                    SUCCESS -> {
//                        result.data?.let { songs ->
//                            if(curPlayingSong == null && songs.isNotEmpty()) {
//                                curPlayingSong = songs[0]
//                                updateTitleAndSongImage(songs[0])
//                            }
//                        }
//                    }
//                    else -> Unit
//                }
//            }
//        }
        viewModel.curPlayingDua.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toDua()
            updateTitleAndSongImage(curPlayingSong!!)
        }
        viewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
//            ivPlayPauseDetail.setImageResource(
//                if(playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
//            )
            mBinding.seek.progress = it?.position?.toInt() ?: 0
        }
        viewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                mBinding.seek.progress = it.toInt()
                setCurPlayerTimeToTextView(it)
            }
        }
        viewModel.curDuaDuration.observe(viewLifecycleOwner) {
            mBinding.seek.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            mBinding.totalTime.text = dateFormat.format(it)
        }
    }


    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        mBinding.currentTime.text = dateFormat.format(ms)
    }
}