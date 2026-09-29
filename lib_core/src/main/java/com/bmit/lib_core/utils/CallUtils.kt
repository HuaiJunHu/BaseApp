package com.bmit.lib_core.utils

import android.telecom.Call

/**
 * Author: created by huhuaijun on 2026/9/29 10:01
 * Function:
 */

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

fun Call.isBluetoothCall(): Boolean {
    val phoneAccountHandle = details.accountHandle
    return phoneAccountHandle?.componentName?.packageName == "com.android.bluetooth.hfpclient.connserv.HfpClientConnectionService"
}

fun Call.number(): String {
    return details.handle?.schemeSpecificPart ?: ""
}

fun Call.displayName(): String {
    return details?.contactDisplayName ?: ""
}

fun Call.id(): String {
    return runCatching {
        val method = details?.javaClass?.getMethod("getTelecomCallId")
        (method?.invoke(details) as? String) ?: ""
    }.onFailure { "" }.toString()

}


fun Call.info() {
    LogUtil.i("detail: id = [${id()}], number = [${number()}], displayName = [${displayName()}], state = [${stateString()}], isConference = [${isConferenceCall()}]")
}