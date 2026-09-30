package com.bmit.lib_core.utils

import android.app.Activity
import android.graphics.Color
import android.view.WindowManager
import androidx.core.view.WindowCompat

/**
 * Author: created by huhuaijun on 2026/9/28 14:49
 * Function:
 */

fun transparentStatusBar(activity: Activity,
    isDark: Boolean = false,
    isFullScreen: Boolean = true,
) {
    val window = activity.window
    // 1. 设置让内容延伸到状态栏和导航栏下方（全屏布局），但不会隐藏状态栏
    WindowCompat.setDecorFitsSystemWindows(window, !isFullScreen)

    // 2. 清除半透明标志，启用系统栏背景绘制
//    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

    // 3. 设置透明颜色
    window.statusBarColor = Color.TRANSPARENT

    // 4. 使用官方最新的控制器来控制状态栏/导航栏的文字颜色
    val decorView = window.decorView
    val controller = WindowCompat.getInsetsController(window, decorView)

    // 控制状态栏文本颜色：true 为深色（黑），false 为浅色（白）
    controller.isAppearanceLightStatusBars = !isDark

    // 控制导航栏文本颜色（如果导航栏是透明的，建议也配置一下）
    controller.isAppearanceLightNavigationBars = !isDark
}