package com.bmit.core.base

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import com.bmit.core.manager.SystemManager
import com.bmit.core.common.GlobalApp
import com.bmit.core.common.LogUtil
import com.bmit.core.common.isDarkMode

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
        registerActivityLifecycle()
        SystemManager.init()


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

    private fun registerActivityLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                SystemManager.activities.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                SystemManager.activities.remove(activity)
            }
        })
    }

}