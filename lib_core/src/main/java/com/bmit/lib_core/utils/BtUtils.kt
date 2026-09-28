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

fun Call.number(): String{
    return details.handle?.schemeSpecificPart ?: ""
}

fun Call.displayName(): String{
    return details?.contactDisplayName ?: ""
}

fun Call.id(): String{
    return details?.telecomCallId ?: ""
}



fun Call.info(){
    LogUtil.i("detail: id = [${details.telecomCallId}], number = [${details.handle?.schemeSpecificPart}], displayName = [${details.contactDisplayName}], state = [${this.stateString()}], isConference = [${this.isConferenceCall()}]")
}



