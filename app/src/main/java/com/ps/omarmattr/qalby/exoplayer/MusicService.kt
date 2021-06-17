package com.ps.omarmattr.qalby.exoplayer

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.IMediaControllerCallback
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.BundleCompat
import androidx.media.MediaBrowserProtocol.EXTRA_SESSION_BINDER
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.ps.omarmattr.qalby.exoplayer.callbacks.MusicPlaybackPreparer
import com.ps.omarmattr.qalby.exoplayer.callbacks.MusicPlayerEventListener
import com.ps.omarmattr.qalby.exoplayer.callbacks.MusicPlayerNotificationListener
import com.ps.omarmattr.qalby.other.MEDIA_ROOT_ID
import com.ps.omarmattr.qalby.other.NETWORK_ERROR
import com.ps.omarmattr.qalby.repository.DuaRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


private const val SERVICE_TAG = "MusicService"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    @Inject
    lateinit var dataSourceFactory: DefaultDataSourceFactory

    @Inject
    lateinit var exoPlayer: SimpleExoPlayer

    @Inject
    lateinit var repository: DuaRepository
    var musicSource: MusicSource? = null

    private lateinit var musicNotificationManager: MusicNotificationManager

    private val serviceJob = Job()
    val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var mediaSessionConnector: MediaSessionConnector

    var isForegroundService = false

    private var curPlayingSong: MediaMetadataCompat? = null

    private var isPlayerInitialized = false


    companion object {
        var curDuaDuration = 0L
            private set
        var musicPlayerEventListener: MusicPlayerEventListener? = null
        var notificationManager: MusicPlayerNotificationListener? = null
        var isInit = false
    }

    override fun setSessionToken(token: MediaSessionCompat.Token?) {
        super.setSessionToken(token)
    }

    override fun onCreate() {
        super.onCreate()
        musicSource = MusicSource(repository.gatAllDua)
        serviceScope.launch {
            musicSource!!.fetchMediaData {}
        }

        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        mediaSession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }


        sessionToken = mediaSession?.sessionToken
        onBind()
    }

    fun onBind() {
        notificationManager = MusicPlayerNotificationListener(this)
        musicNotificationManager = MusicNotificationManager(
            this,
            mediaSession?.sessionToken!!,
            notificationManager!!
        ) {
            curDuaDuration = exoPlayer.duration
        }

        val musicPlaybackPreparer = MusicPlaybackPreparer(musicSource!!) {
            curPlayingSong = it
            preparePlayer(
                musicSource!!.duaList,
                it,
                true
            )
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession!!)
        mediaSessionConnector.setPlaybackPreparer(musicPlaybackPreparer)
        mediaSessionConnector.setQueueNavigator(MusicQueueNavigator())
        mediaSessionConnector.setPlayer(exoPlayer)

        musicPlayerEventListener = MusicPlayerEventListener(this)
        exoPlayer.addListener(musicPlayerEventListener!!)
        musicNotificationManager.showNotification(exoPlayer)
    }


    private inner class MusicQueueNavigator : TimelineQueueNavigator(mediaSession!!) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicSource!!.duaList[windowIndex].description
        }
    }

    private fun preparePlayer(
        duaList: List<MediaMetadataCompat>,
        itemToPlay: MediaMetadataCompat?,
        playNow: Boolean
    ) {
        if (isInit) {
            musicSource!!.duaList = emptyList()
            musicSource = MusicSource(repository.gatAllDua)
            serviceScope.launch {
                musicSource!!.fetchMediaData {
                    CoroutineScope(Dispatchers.Main).launch {
                        onBind()
                    }
                }
            }

            isInit = false
        }
        val curSongIndex = if (curPlayingSong == null) 0 else duaList.indexOf(itemToPlay)

        exoPlayer.prepare(musicSource!!.asMediaSource(dataSourceFactory))
        exoPlayer.seekTo(curSongIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoPlayer.removeListener(musicPlayerEventListener!!)
        exoPlayer.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultsSent = musicSource!!.whenReady { isInitialized ->
                    if (isInitialized) {
                        result.sendResult(musicSource!!.asMediaItems())
                        if (!isPlayerInitialized && musicSource!!.duaList.isNotEmpty()) {
                            preparePlayer(musicSource!!.duaList, musicSource!!.duaList[0], false)
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession!!.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendResult(null)
                    }
                }
                if (!resultsSent) {
                    result.detach()
                }
            }
        }
    }
}