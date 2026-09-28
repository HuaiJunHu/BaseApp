package com.bmit.lib_core

import android.app.Activity
import android.bluetooth.BluetoothA2dpSink
import android.bluetooth.BluetoothAvrcpController
import android.bluetooth.BluetoothHeadsetClient
import android.bluetooth.BluetoothPbapClient
import android.bluetooth.BluetoothProfile
import android.graphics.Color
import android.media.MediaMetadata
import android.media.MediaMetadata.METADATA_KEY_ALBUM_ART_URI
import android.media.MediaMetadata.METADATA_KEY_ARTIST
import android.media.MediaMetadata.METADATA_KEY_DURATION
import android.media.MediaMetadata.METADATA_KEY_TITLE
import android.media.session.PlaybackState
import android.os.SystemClock
import android.telecom.Call
import android.view.View
import android.view.WindowManager
import androidx.core.view.WindowCompat

/**
 * Author: created by huhuaijun on 2026/9/24 17:50
 * Function:
 */
fun setTransparentStatusBar(
    activity: Activity,
    isDark: Boolean = false,
    isFullScreen: Boolean = true,
) {
    val window = activity.window

    // 1. 设置让内容延伸到状态栏和导航栏下方（全屏布局），但不会隐藏状态栏
    WindowCompat.setDecorFitsSystemWindows(window, !isFullScreen)

    // 2. 清除半透明标志，启用系统栏背景绘制
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    // 3. 设置透明颜色
    window.statusBarColor = Color.TRANSPARENT

    // 4. 使用官方最新的控制器来控制状态栏/导航栏的文字颜色
    val decorView = window.decorView
    val controller = WindowCompat.getInsetsController(window, decorView)

    // 控制状态栏文本颜色：true 为深色（黑），false 为浅色（白）
    controller.isAppearanceLightStatusBars = !isDark

    // 控制导航栏文本颜色（如果导航栏是透明的，建议也配置一下）
    controller.isAppearanceLightNavigationBars = !isDark
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

/**
 * 蓝牙连接状态
 */
fun Int.toBluetoothState(): String {
    return when (this) {
        BluetoothProfile.STATE_DISCONNECTED -> "DISCONNECTED"
        BluetoothProfile.STATE_CONNECTING -> "CONNECTING"
        BluetoothProfile.STATE_CONNECTED -> "CONNECTED"
        BluetoothProfile.STATE_DISCONNECTING -> "DISCONNECTING"
        else -> "DISCONNECTED"
    }
}

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
 * 蓝牙协议连接状态
 */
fun String.bluetoothActionType(): String{
    return when(this){
        BluetoothA2dpSink.ACTION_CONNECTION_STATE_CHANGED -> "A2DP SINK"
        BluetoothAvrcpController.ACTION_CONNECTION_STATE_CHANGED -> "AVRCP"
        BluetoothHeadsetClient.ACTION_CONNECTION_STATE_CHANGED -> "HEADSET"
        BluetoothHeadsetClient.ACTION_AUDIO_STATE_CHANGED -> "HEADSET AUDIO"
        BluetoothPbapClient.ACTION_CONNECTION_STATE_CHANGED-> "PBAP"
        else -> "NONE"

    }
}


fun View.setDebounceClickListener(
    interval: Long = 500L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()

        if (currentTime - lastClickTime < interval) {
            return@setOnClickListener
        }

        lastClickTime = currentTime
        onClick(view)
    }
}


//=============================蓝牙电话相关 ===========================
fun Call.stateString(): String {
    return when (state) {
        Call.STATE_NEW -> "STATE_NEW"
        Call.STATE_DIALING -> "STATE_DIALING"
        Call.STATE_RINGING -> "STATE_RINGING"
        Call.STATE_HOLDING -> "STATE_HOLDING"
        Call.STATE_ACTIVE -> "STATE_ACTIVE"
        Call.STATE_DISCONNECTED -> "STATE_DISCONNECTED"
        Call.STATE_CONNECTING -> "STATE_CONNECTING"
        else -> "other"
    }
}

fun Call.isConferenceCall(): Boolean {
    return details?.hasProperty(Call.Details.PROPERTY_CONFERENCE) == true
}

fun Call.isBluetoothCall(): Boolean{
    val phoneAccountHandle = details.accountHandle
    return phoneAccountHandle?.componentName?.packageName == "com.android.bluetooth.hfpclient.connserv.HfpClientConnectionService"
}

fun Call.info(){
    LogUtil.i("detail: id = [${details.telecomCallId}], number = [${details.handle?.schemeSpecificPart}], displayName = [${details.contactDisplayName}], state = [${this.stateString()}], isConference = [${this.isConferenceCall()}]")
}


