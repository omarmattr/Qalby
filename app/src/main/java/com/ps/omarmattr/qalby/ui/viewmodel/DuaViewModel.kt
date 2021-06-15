package com.ps.omarmattr.qalby.ui.viewmodel

import android.app.Application
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ps.omarmattr.qalby.exoplayer.*
import com.ps.omarmattr.qalby.model.dua.DuaRequestItem
import com.ps.omarmattr.qalby.other.UPDATE_PLAYER_POSITION_INTERVAL
import com.ps.omarmattr.qalby.repository.DuaRepository
import com.ps.omarmattr.qalby.util.ResultRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuaViewModel @Inject constructor(
    val duaRepository: DuaRepository,

    application: Application
) :
    AndroidViewModel(application) {


    fun getDua(id: Int) {
        duaRepository.getDua(
            id
        )
    }

    val listDuaStateFlow: StateFlow<ResultRequest<Any>> = duaRepository.listDuaStateFlow


     var playbackState: LiveData<PlaybackStateCompat?>?=null


    private val _curDuaDuration = MutableLiveData<Long>()
    val curDuaDuration: LiveData<Long> = _curDuaDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition: LiveData<Long> = _curPlayerPosition


    fun updateCurrentPlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val pos = playbackState?.value?.currentPlaybackPosition
                if (curPlayerPosition.value != pos) {
                    _curPlayerPosition.postValue(pos)
                    _curDuaDuration.postValue(MusicService.curDuaDuration)
                }
                delay(UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }

    var curPlayingDua: LiveData<MediaMetadataCompat?>? = null


    fun playOrToggleDua(
        mediaItem: DuaRequestItem,
        musicServiceConnection: MusicServiceConnection,
        toggle: Boolean = false
    ) {
        val isPrepared = playbackState?.value?.isPrepared ?: false
        if (isPrepared && mediaItem.id.toString() ==
            curPlayingDua?.value?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
        ) {
            playbackState?.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> if (toggle) musicServiceConnection.transportControls.pause()
                    playbackState.isPlayEnabled -> musicServiceConnection.transportControls.play()
                    else -> Unit
                }
            }
        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.id.toString(), null)
        }
    }


    fun skipToNextSong(musicServiceConnection: MusicServiceConnection) {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousSong(musicServiceConnection: MusicServiceConnection) {
        musicServiceConnection.transportControls.skipToPrevious()
    }

}