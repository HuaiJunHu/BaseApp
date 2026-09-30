package com.bmit.lib_core.utils

import android.content.Context
import android.media.AudioFocusInfo

/**
 * Author: created by huhuaijun on 2026/9/29 14:11
 * Function:
 */

fun Context.isDarkMode(): Boolean{
    val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
}
fun AudioFocusInfo.toInfo(){
    LogUtil.d( """
        packageName = $packageName
        clientId    = $clientId
        attributes  = ${attributes}
        """.trimIndent())
}