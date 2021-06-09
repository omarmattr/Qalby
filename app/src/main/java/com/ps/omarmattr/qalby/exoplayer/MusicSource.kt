package com.ps.omarmattr.qalby.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import androidx.core.net.toUri
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.ps.omarmattr.qalby.repository.DuaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicSource @Inject constructor(
    private val repository: DuaRepository
) {
    var duaList = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        val allDua = repository.gatAllDua
        duaList = allDua.map { dua ->
            Log.e("ttttttttttttt",dua.maleAudioUrl?:dua.femaleAudioUrl?:"")
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, dua.name)
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, dua.id.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, dua.name)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, dua.name)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, "https://t4.ftcdn.net/jpg/02/31/84/57/360_F_231845740_fBDGBrUBpjxMAizPcozCmlECbkox7GJd.jpg")
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, dua.maleAudioUrl?:dua.femaleAudioUrl?:"")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, "https://t4.ftcdn.net/jpg/02/31/84/57/360_F_231845740_fBDGBrUBpjxMAizPcozCmlECbkox7GJd.jpg")
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, dua.name)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, dua.name)
                .build()
        }
        state = State.STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        duaList.forEach { dua ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(
                    dua.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri()
                )
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = duaList.map { dua ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(dua.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(dua.description.title)
            .setSubtitle(dua.description.subtitle)
            .setMediaId(dua.description.mediaId)
            .setIconUri(dua.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING) {
            onReadyListeners += action
            return false
        } else {
            action(state == State.STATE_INITIALIZED)
            return true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}
