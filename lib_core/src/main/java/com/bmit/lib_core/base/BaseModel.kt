package com.bmit.lib_core.base

import androidx.lifecycle.ViewModel
import com.bmit.lib_core.manager.SystemManager
import com.bmit.lib_core.utils.LogUtil

/**
 * Author: created by huhuaijun on 2026/9/29 14:18
 * Function:
 */
abstract class BaseModel : ViewModel() {
    var language = SystemManager.languageFlow.value
        set(value) {
            LogUtil.d { "language change: old = [$field], new = [$value]" }
            field = value
        }
    var isDarkMode = SystemManager.darkModeFlow.value
        set(value) {
            LogUtil.d { "darkMode change: old = [$field], new = [$value]" }
            field = value
        }


}