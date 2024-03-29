package com.ps.omarmattr.qalby.exoplayer.callbacks

import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.ps.omarmattr.qalby.exoplayer.MusicService

class MusicPlayerEventListener(
    val musicService: MusicService
) : Player.EventListener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == Player.STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        Log.e("tytttttttttttt", error.message.toString())
        error.printStackTrace()
        Toast.makeText(musicService, "An unknown error occured", Toast.LENGTH_LONG).show()
    }
}