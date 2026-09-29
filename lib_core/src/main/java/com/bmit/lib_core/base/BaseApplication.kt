package com.bmit.lib_core.base

import android.app.Application
import android.content.res.Configuration
import com.bmit.lib_core.manager.SystemManager
import com.bmit.lib_core.utils.GlobalApp
import com.bmit.lib_core.utils.LogUtil
import com.bmit.lib_core.utils.isDarkMode

/**
 * Author: created by huhuaijun on 2026/9/29 13:54
 * Function:
 */
class BaseApplication: Application() {
    var currentLanguageTag = SystemManager.languageFlow.value
    var currentDarkMode = SystemManager.darkModeFlow.value

    override fun onCreate() {
        super.onCreate()
        LogUtil.i("Application onCreate")

    }

    override fun onLowMemory() {
        super.onLowMemory()
        LogUtil.i("Application onLowMemory")
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val changeLanguage = newConfig.locales[0].toLanguageTag()
        if (changeLanguage!= currentLanguageTag) {
            LogUtil.i("Application onConfigurationChanged: language change from $currentLanguageTag to $changeLanguage")
            currentLanguageTag =changeLanguage
            SystemManager.updateLocale(currentLanguageTag)
        }
        if (GlobalApp.isDarkMode() != currentDarkMode){
            LogUtil.i("Application onConfigurationChanged: dark mode change from $currentDarkMode to ${GlobalApp.isDarkMode()}")
            currentDarkMode = GlobalApp.isDarkMode()
            SystemManager.updateDarkMode(currentDarkMode)
        }
    }
}