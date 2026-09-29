package com.bmit.lib_core.utils

import android.bluetooth.BluetoothA2dpSink
import android.bluetooth.BluetoothAvrcpController
import android.bluetooth.BluetoothHeadsetClient
import android.bluetooth.BluetoothPbapClient
import android.bluetooth.BluetoothProfile
import android.telecom.Call

/**
 * Author: created by huhuaijun on 2026/9/24 17:50
 * Function:
 */

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
 * 蓝牙协议连接状态
 */
fun String.bluetoothActionType(): String {
    return when (this) {
        BluetoothA2dpSink.ACTION_CONNECTION_STATE_CHANGED -> "A2DP SINK"
        BluetoothAvrcpController.ACTION_CONNECTION_STATE_CHANGED -> "AVRCP"
        BluetoothHeadsetClient.ACTION_CONNECTION_STATE_CHANGED -> "HEADSET"
        BluetoothHeadsetClient.ACTION_AUDIO_STATE_CHANGED -> "HEADSET AUDIO"
        BluetoothPbapClient.ACTION_CONNECTION_STATE_CHANGED -> "PBAP"
        else -> "NONE"

    }
}




