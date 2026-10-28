package com.bmit.core.common

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Process
import android.provider.Settings

/**
 * Author: created by huhuaijun on 2026/9/24 18:07
 * Function:
 */
val GlobalApp by lazy {
    @Suppress("PrivateApi")
    Class.forName("android.app.ActivityThread").let {
        it.getDeclaredField("mInitialApplication").apply {
            isAccessible = true
        }.get(
            it.getDeclaredMethod("currentActivityThread").invoke(null)
        ) as Application
    }
}

val MainHandler by lazy { Handler(Looper.getMainLooper()) }

private val BgHandler by lazy {
    Handler(
        HandlerThread("BgThread", Process.THREAD_PRIORITY_BACKGROUND)
            .apply { start() }.looper
    )
}

fun post2Main(task: () -> Unit) = MainHandler.post(task)

fun post2MainWithShortDelay(task: () -> Unit, delay: Long  =0) = MainHandler.postDelayed(task, delay)

fun post2Bg(task: () -> Unit) = BgHandler.post(task)

fun createMainHandler() = Handler(Looper.getMainLooper())
fun createBgHandler() = Handler(BgHandler.looper)

fun isMainLooper() = Looper.myLooper() === Looper.getMainLooper()

fun runOnMain(task: () -> Unit) {
    if (isMainLooper()) {
        task()
    } else {
        post2Main(task)
    }
}

fun runOnMainWhenIdle(task: () -> Unit) = Looper.getMainLooper().queue.addIdleHandler {
    task()
    false
}


fun Context.isDarkMode(): Boolean{
    val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

fun getThemeMode() : Int{
    return Settings.System.getInt(
        GlobalApp.contentResolver,
        "bmi_system_theme",
        0
    )
}

