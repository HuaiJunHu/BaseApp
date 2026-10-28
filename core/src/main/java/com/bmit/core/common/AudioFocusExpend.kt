package com.bmit.core.common

import android.media.AudioFocusInfo

/**
 * Author: created by huhuaijun on 2026/9/29 14:11
 * Function:
 */

fun AudioFocusInfo.toInfo(){
    LogUtil.d( """
        packageName = $packageName
        clientId    = $clientId
        attributes  = ${attributes}
        """.trimIndent())
}