package com.bmit.lib_core.utils

import android.media.MediaMetadata
import android.media.MediaMetadata.METADATA_KEY_ALBUM_ART_URI
import android.media.MediaMetadata.METADATA_KEY_ARTIST
import android.media.MediaMetadata.METADATA_KEY_DURATION
import android.media.MediaMetadata.METADATA_KEY_TITLE
import android.media.session.PlaybackState

/**
 * Author: created by huhuaijun on 2026/9/28 14:46
 * Function:
 */

/**
 * 播放状态
 */
fun Int.toPlayBackState(): String {
    return when (this) {
        PlaybackState.STATE_SKIPPING_TO_QUEUE_ITEM -> "SKIPPING_TO_QUEUE_ITEM"
        PlaybackState.STATE_SKIPPING_TO_NEXT -> "SKIPPING_TO_NEXT"
        PlaybackState.STATE_SKIPPING_TO_PREVIOUS -> "SKIPPING_TO_PREVIOUS"
        PlaybackState.STATE_CONNECTING -> "CONNECTING"
        PlaybackState.STATE_ERROR -> "ERROR"
        PlaybackState.STATE_BUFFERING -> "BUFFERING"
        PlaybackState.STATE_REWINDING -> "REWINDING"
        PlaybackState.STATE_FAST_FORWARDING -> "FAST_FORWARDING"
        PlaybackState.STATE_PLAYING -> "PLAYING"
        PlaybackState.STATE_PAUSED -> "PAUSED"
        PlaybackState.STATE_STOPPED -> "STOPPED"
        PlaybackState.STATE_NONE -> "NONE"
        else -> "NONE"
    }
}


/**
 * 媒体元数据标题
 */
fun MediaMetadata.getTitle(): String? {
    return getString(METADATA_KEY_TITLE)
}

/**
 * 媒体元数据歌手
 */
fun MediaMetadata.getArtist(): String? {
    return getString(METADATA_KEY_ARTIST)
}

/**
 * 媒体元数据封面
 */
fun MediaMetadata.getAlbum(): String? {
    return getString(METADATA_KEY_ALBUM_ART_URI)
}

/**
 * 媒体元数据的持续时长
 */
fun MediaMetadata.getDuration(): Long {
    return getLong(METADATA_KEY_DURATION)
}
