package com.ps.omarmattr.qalby.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.ps.omarmattr.qalby.model.dua.DuaRequestItem


fun MediaMetadataCompat.toDua(): DuaRequestItem? {
    return description?.let {
        DuaRequestItem(
            ayat = it.title.toString(),
            femaleAudioUrl = it.mediaUri.toString(),
            id = it.mediaId?.toInt() ?: 0,
            isHifz = false,
            isSaved = false,
            maleAudioUrl = it.mediaUri.toString(),
            name = it.title.toString(),
            it.title.toString(),
            it.title.toString(),
            it.title.toString(),
        )
    }
}