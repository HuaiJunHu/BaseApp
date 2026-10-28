package com.bmit.core.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.provider.Settings
import com.bmit.core.common.GlobalApp
import com.bmit.core.common.MainHandler
import com.bmit.core.common.getThemeMode
import com.bmit.core.receiver.LanguageReceiver
import com.bmit.core.common.isDarkMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Author: created by huhuaijun on 2026/9/29 11:45
 * Function:
 */
object SystemManager {
    @SuppressLint("ConstantLocale")
    private val _languageFlow = MutableStateFlow(Locale.getDefault().toLanguageTag())
    val languageFlow: StateFlow<String> = _languageFlow.asStateFlow()

    private val _darkModeFlow = MutableStateFlow(GlobalApp.isDarkMode())
    val darkModeFlow: StateFlow<Boolean> = _darkModeFlow.asStateFlow()
    private val _defaultThemeFlow = MutableStateFlow(getThemeMode())

    val defaultThemeFlow = _defaultThemeFlow.asStateFlow()

    val activities = mutableListOf<Activity>()


    fun init(){
        initBroadcast()
        registerThemeObserver()
    }

    private fun initBroadcast(){
        val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        GlobalApp.registerReceiver(LanguageReceiver(), filter)
    }


    fun updateLocale(newLanguage: String) {
        _languageFlow.value = newLanguage
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        _darkModeFlow.value = isDarkMode
    }

    fun updateTheme(theme: Int){
        _defaultThemeFlow.value = theme
    }

    private fun registerThemeObserver() {
        val themeObserver = object : ContentObserver(MainHandler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                activities.forEach { it.finish() }
                activities.clear()
                updateTheme(getThemeMode())
            }
        }
        GlobalApp.contentResolver.registerContentObserver(
            Settings.System.getUriFor("bmi_system_theme"),
            true,
            themeObserver
        )
    }


}