package com.bmit.lib_core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bmit.lib_core.manager.SystemManager

/**
 * Author: created by huhuaijun on 2026/9/29 11:41
 * Function:
 */
class LanguageReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_LOCALE_CHANGED == intent.action) {
            val currentLocale = context.resources.configuration.locales[0].toLanguageTag()
            SystemManager.updateLocale(currentLocale)
        }
    }
}