package com.bmit.lib_core.expend

import android.media.AudioFocusInfo
import com.bmit.lib_core.utils.LogUtil

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