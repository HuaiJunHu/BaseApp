package com.bmit.lib_core.utils


import android.os.SystemClock
import android.view.View

/**
 * Author: created by huhuaijun on 2026/9/24 17:50
 * Function:
 */


fun View.setDebounceClickListener(
    interval: Long = 500L,
    onClick: (View) -> Unit
) {
    var lastClickTime = 0L

    setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()

        if (currentTime - lastClickTime < interval) {
            return@setOnClickListener
        }

        lastClickTime = currentTime
        onClick(view)
    }
}

