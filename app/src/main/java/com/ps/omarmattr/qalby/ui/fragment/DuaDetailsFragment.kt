package com.ps.omarmattr.qalby.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.ps.omarmattr.qalby.BR
import com.ps.omarmattr.qalby.R
import com.ps.omarmattr.qalby.adapter.GenericAdapter
import com.ps.omarmattr.qalby.databinding.FragmentDuaDetailsBinding
import com.ps.omarmattr.qalby.exoplayer.MusicService
import com.ps.omarmattr.qalby.exoplayer.MusicServiceConnection
import com.ps.omarmattr.qalby.exoplayer.isPlaying
import com.ps.omarmattr.qalby.exoplayer.toDua
import com.ps.omarmattr.qalby.model.Dua
import com.ps.omarmattr.qalby.model.dua.DuaRequestItem
import com.ps.omarmattr.qalby.other.DATA_DETAILS
import com.ps.omarmattr.qalby.other.MEDIA_ROOT_ID
import com.ps.omarmattr.qalby.ui.viewmodel.DuaViewModel
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DuaDetailsFragment : Fragment(), GenericAdapter.OnListItemViewClickListener<DuaRequestItem> {

    private val mBinding by lazy {
        FragmentDuaDetailsBinding.inflate(layoutInflater)
    }

    private val mAdapter by lazy {
        GenericAdapter(R.layout.item_dua_details, BR.duaRequest, this)
    }


    @Inject
    lateinit var viewModel: DuaViewModel

    @Inject
    lateinit var glide: RequestManager


    private var curPlayingSong: DuaRequestItem? = null
    private var playbackState: PlaybackStateCompat? = null
    private var connection: MusicServiceConnection? = null

    private lateinit var dua: Dua
    private var position = 0

    private var shouldUpdateSeekbar = true
    private var isLoading = false
    private var isPlay = false
    private var isRepeat = false

    var dataList: List<DuaRequestItem>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = mBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().apply {
            dua = getParcelable(DATA_DETAILS)!!
        }
        mBinding.title.text = dua.name

        mBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


        mBinding.btnPlay.setOnClickListener {
            curPlayingSong?.let {
                viewModel.playOrToggleDua(it, connection!!, true)
            }
            isPlay = !isPlay
        }

        mBinding.btnRepeat.setOnClickListener {
            isRepeat = !isRepeat
            if (isRepeat)
                mBinding.btnRepeat.setImageResource(R.drawable.ic_repeat)
            else
                mBinding.btnRepeat.setImageResource(R.drawable.ic_not_repeat)
        }


        mBinding.seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.e("tttttttt", "onProgressChanged")
                if (fromUser) {
                    setCurPlayerTimeToTextView(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Log.e("tttttttt", "onStartTrackingTouch")
                shouldUpdateSeekbar = false
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                Log.e("tttttttt", "onStopTrackingTouch")
                seekBar?.let {
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
                    this@DuaDetailsFragment.position = position
                    if (isPlay) {
                        curPlayingSong?.let {
                            viewModel.playOrToggleDua(it, connection!!, true)
                        }
                        isPlay = false
                    }
                    mBinding.size.text = "${position + 1}/${mAdapter.data.size}"
                    curPlayingSong = dataList!![position]
                    updateTitleAndSongImage(curPlayingSong!!)

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
                        if (isLoading) {
                            val data = it.data
                            dataList = data as List<DuaRequestItem>
                            mAdapter.data = dataList!!
                            mBinding.size.text = "1/${data.size}"
                            mAdapter.notifyDataSetChanged()
                            connection = MusicServiceConnection(requireContext())
                            viewModel.curPlayingDua = connection!!.curPlayingSong
                            viewModel.playbackState = connection!!.playbackState
                            viewModel.updateCurrentPlayerPosition()
                            subscribeToObservers()
                            isLoading = false
                        }
                    }
                    ResultRequest.Status.LOADING -> {
                        isLoading = true
                    }
                    ResultRequest.Status.ERROR -> {
                    }
                }
            }
        }


        mBinding.next.setOnClickListener {
            viewModel.skipToPreviousSong(connection!!)
            mBinding.viewPager.currentItem = mBinding.viewPager.currentItem + 1
        }

        mBinding.previous.setOnClickListener {
            viewModel.skipToNextSong(connection!!)
            mBinding.viewPager.currentItem = mBinding.viewPager.currentItem - 1
        }
    }

    override fun onClickItem(itemViewModel: DuaRequestItem, type: Int, position: Int) {
    }

    private fun updateTitleAndSongImage(dua: DuaRequestItem) {
        val title = "${dua.name} - ${dua.ayat}"
    }


    private fun subscribeToObservers() {
        viewModel.curPlayingDua?.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingSong = it.toDua()
            updateTitleAndSongImage(curPlayingSong!!)
        }
        viewModel.playbackState?.observe(viewLifecycleOwner) {

            playbackState = it
            mBinding.btnPlay.setImageResource(
                if (playbackState?.isPlaying == true) R.drawable.ic_pause else {
                    R.drawable.ic_play
                }
            )
            mBinding.seek.progress = it?.position?.toInt() ?: 0
        }
        viewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekbar) {
                it?.let { pro ->
                    mBinding.seek.progress = pro.toInt()
                    setCurPlayerTimeToTextView(it)
                }
            }
        }
        viewModel.curDuaDuration.observe(viewLifecycleOwner) {
            mBinding.seek.max = it.toInt()
            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            totalTime = dateFormat.format(it)
            mBinding.totalTime.text = totalTime

        }
    }

    var totalTime = ""
    private fun setCurPlayerTimeToTextView(ms: Long) {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        mBinding.currentTime.text = dateFormat.format(ms)
        if (mBinding.currentTime.text.toString() == totalTime && totalTime.isNotEmpty()) {
            Log.e("tttttttttttt", totalTime)
            totalTime = ""
            position++
            mBinding.viewPager.currentItem = position
            if (isPlay) {
                curPlayingSong?.let {
                    viewModel.playOrToggleDua(it, connection!!, true)
                }
                isPlay = false
            }
        }
    }

    override fun onDestroy() {
        viewModel.curPlayingDua = null
        viewModel.playbackState = null
        if (playbackState?.isPlaying == false)
            curPlayingSong?.let {
                viewModel.playOrToggleDua(it, connection!!, true)
            }
        MusicService.notificationManager?.let {
            it.musicService.apply {
                stopForeground(true)
                isForegroundService = false
                stopSelf()
                exoPlayer.stop()
                MusicService.isInit = true
            }
        }
        super.onDestroy()
    }
}