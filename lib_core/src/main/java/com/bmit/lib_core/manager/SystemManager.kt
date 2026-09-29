package com.bmit.lib_core.manager

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import com.bmit.lib_core.utils.GlobalApp
import com.bmit.lib_core.receiver.LanguageReceiver
import com.bmit.lib_core.utils.isDarkMode
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


    init {

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


}