package com.bmit.lib_core

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

/**
 * Android 日志工具类
 *
 * 特性：
 * - 日志开关
 * - 日志级别
 * - ClassLogger
 * - Caller 信息
 * - Thread 信息
 * - Android Studio 点击跳转
 * - JSON 格式化
 * - 超长日志分段
 * - 耗时统计
 * - 懒加载日志
 * - StackTrace 输出
 *
 * created by huhuaijun on 2025/11/27
 */
object LogUtil {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    const val DEFAULT_TAG = "DEFAULT_TAG"

    const val VERSION = "1.0.0"

    private const val MAX_LOG_LENGTH = 4000

    private const val UNKNOWN = "unknown"

    private val LOG_UTIL_CLASS_NAME =
        LogUtil::class.java.name

    private val LOG_UTIL_PACKAGE =
        LogUtil::class.java.`package`?.name.orEmpty()

    // ------------------------------------------------------------------------
    // Log Level
    // ------------------------------------------------------------------------

    enum class Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    // ------------------------------------------------------------------------
    // Config
    // ------------------------------------------------------------------------

    /**
     * 使用不可变 Config。
     *
     * 配置修改时整体替换 Config，
     * 避免多线程下修改 Config 内部字段导致可见性问题。
     */
    data class Config(
        val enabled: Boolean = true,
        val globalTag: String = DEFAULT_TAG,
        val showThreadInfo: Boolean = false,
        val showCallerInfo: Boolean = true,
        val clickableLinks: Boolean = false,
        val dateFormat: String = "HH:mm:ss.SSS"
    )

    @Volatile
    var config: Config = Config()
        private set

    // ------------------------------------------------------------------------
    // Cache
    // ------------------------------------------------------------------------

    /**
     * ClassLogger 缓存。
     */
    private val classLoggers =
        ConcurrentHashMap<Class<*>, ClassLogger>()

    /**
     * DateTimeFormatter 缓存。
     *
     * DateTimeFormatter 本身是线程安全的，
     * 不需要 ThreadLocal。
     */
    @Volatile
    private var cachedDateFormat: String = config.dateFormat

    @Volatile
    private var cachedDateFormatter: DateTimeFormatter =
        createDateFormatter(config.dateFormat)

    private fun createDateFormatter(
        pattern: String
    ): DateTimeFormatter {
        return try {
            DateTimeFormatter.ofPattern(
                pattern,
                Locale.getDefault()
            )
        } catch (e: IllegalArgumentException) {
            DateTimeFormatter.ofPattern(
                "HH:mm:ss.SSS",
                Locale.getDefault()
            )
        }
    }

    private fun getDateFormatter(): DateTimeFormatter {
        val currentPattern = config.dateFormat

        if (currentPattern == cachedDateFormat) {
            return cachedDateFormatter
        }

        synchronized(this) {
            if (currentPattern != cachedDateFormat) {
                cachedDateFormatter =
                    createDateFormatter(currentPattern)

                cachedDateFormat = currentPattern
            }
        }

        return cachedDateFormatter
    }

    // ------------------------------------------------------------------------
    // Basic Log
    // ------------------------------------------------------------------------

    fun v(
        message: String,
        tag: String = config.globalTag
    ) {
        log(
            Level.VERBOSE,
            tag,
            message
        )
    }

    fun d(
        message: String,
        tag: String = config.globalTag
    ) {
        log(
            Level.DEBUG,
            tag,
            message
        )
    }

    fun i(
        message: String,
        tag: String = config.globalTag
    ) {
        log(
            Level.INFO,
            tag,
            message
        )
    }

    fun w(
        message: String,
        tag: String = config.globalTag
    ) {
        log(
            Level.WARN,
            tag,
            message
        )
    }

    fun e(
        message: String,
        throwable: Throwable? = null,
        tag: String = config.globalTag
    ) {
        log(
            Level.ERROR,
            tag,
            message,
            throwable
        )
    }

    // ------------------------------------------------------------------------
    // Lazy Log
    // ------------------------------------------------------------------------

    /**
     * 推荐在高频日志场景使用：
     *
     * LogUtil.d {
     *     "call=$call state=${call.state}"
     * }
     *
     * 当日志关闭时，messageBuilder 不会执行。
     */
    inline fun v(
        tag: String = config.globalTag,
        crossinline messageBuilder: () -> String
    ) {
        if (!config.enabled) {
            return
        }

        v(
            messageBuilder(),
            tag
        )
    }

    inline fun d(
        tag: String = config.globalTag,
        crossinline messageBuilder: () -> String
    ) {
        if (!config.enabled) {
            return
        }

        d(
            messageBuilder(),
            tag
        )
    }

    inline fun i(
        tag: String = config.globalTag,
        crossinline messageBuilder: () -> String
    ) {
        if (!config.enabled) {
            return
        }

        i(
            messageBuilder(),
            tag
        )
    }

    inline fun w(
        tag: String = config.globalTag,
        crossinline messageBuilder: () -> String
    ) {
        if (!config.enabled) {
            return
        }

        w(
            messageBuilder(),
            tag
        )
    }

    inline fun e(
        throwable: Throwable? = null,
        tag: String = config.globalTag,
        crossinline messageBuilder: () -> String
    ) {
        if (!config.enabled) {
            return
        }

        e(
            messageBuilder(),
            throwable,
            tag
        )
    }

    // ------------------------------------------------------------------------
    // Core
    // ------------------------------------------------------------------------

    private fun log(
        level: Level,
        tag: String,
        message: String,
        throwable: Throwable? = null
    ) {
        val currentConfig = config

        if (!currentConfig.enabled) {
            return
        }

        val formattedMessage =
            buildLogMessage(
                message = message,
                config = currentConfig
            )

        when (level) {
            Level.VERBOSE -> {
                Log.v(
                    tag,
                    formattedMessage,
                    throwable
                )
            }

            Level.DEBUG -> {
                Log.d(
                    tag,
                    formattedMessage,
                    throwable
                )
            }

            Level.INFO -> {
                Log.i(
                    tag,
                    formattedMessage,
                    throwable
                )
            }

            Level.WARN -> {
                Log.w(
                    tag,
                    formattedMessage,
                    throwable
                )
            }

            Level.ERROR -> {
                Log.e(
                    tag,
                    formattedMessage,
                    throwable
                )
            }
        }
    }

    // ------------------------------------------------------------------------
    // Message Formatter
    // ------------------------------------------------------------------------

    private fun buildLogMessage(
        message: String,
        config: Config
    ): String {

        /**
         * 最常见场景：
         *
         * showCallerInfo = false
         * showThreadInfo = false
         *
         * 直接返回原消息，避免任何额外开销。
         */
        if (!config.showCallerInfo &&
            !config.showThreadInfo
        ) {
            return message
        }

        val caller =
            if (config.showCallerInfo) {
                getRealCaller()
            } else {
                null
            }

        return buildString(
            message.length + 96
        ) {

            // Thread / Time
            if (config.showThreadInfo) {
                append("[thread:")
                append(Thread.currentThread().name)
                append(",time:")
                append(getCurrentTime())
                append("]")
            }

            // Caller
            if (caller != null) {

                if (config.clickableLinks) {

                    append("at ")
                    append(caller.className)
                    append(".")
                    append(caller.methodName)
                    append("(")
                    append(
                        caller.fileName
                            ?: "Unknown Source"
                    )
                    append(":")
                    append(caller.lineNumber)
                    append(")")

                } else {

                    val simpleClassName =
                        caller.className
                            .substringAfterLast('.')

                    append("[")
                    append(VERSION)
                    append(",")
                    append(simpleClassName)
                    append(".")
                    append(caller.methodName)
                    append(":")
                    append(caller.lineNumber)
                    append("]")
                }
            }

            if (isNotEmpty()) {
                append(": ")
            }

            append(message)
        }
    }

    // ------------------------------------------------------------------------
    // Caller
    // ------------------------------------------------------------------------

    /**
     * 获取真正的业务调用者。
     *
     * 注意：
     * 只有 showCallerInfo=true 时才会调用。
     */
    private fun getRealCaller(): StackTraceElement? {

        val stackTrace =
            Thread.currentThread().stackTrace

        for (element in stackTrace) {

            val className =
                element.className

            if (isInternalClass(className)) {
                continue
            }

            return element
        }

        return null
    }

    /**
     * 判断是否为 LogUtil 内部调用。
     */
    private fun isInternalClass(
        className: String
    ): Boolean {

        return className == "java.lang.Thread" ||
                className == LOG_UTIL_CLASS_NAME ||
                className.startsWith("$LOG_UTIL_CLASS_NAME\$") ||
                className.startsWith("$LOG_UTIL_PACKAGE.")
    }

    // ------------------------------------------------------------------------
    // JSON
    // ------------------------------------------------------------------------

    fun json(
        jsonStr: String,
        tag: String = config.globalTag
    ) {
        if (!config.enabled) {
            return
        }

        val value = jsonStr.trim()

        if (value.isEmpty()) {
            d(
                "JSON: <empty>",
                tag
            )
            return
        }

        try {

            when {

                value.startsWith("{") -> {

                    val json =
                        JSONObject(value)
                            .toString(4)

                    d(
                        "JSON:\n$json",
                        tag
                    )
                }

                value.startsWith("[") -> {

                    val json =
                        JSONArray(value)
                            .toString(4)

                    d(
                        "JSON Array:\n$json",
                        tag
                    )
                }

                else -> {

                    d(
                        "Invalid JSON: $jsonStr",
                        tag
                    )
                }
            }

        } catch (e: JSONException) {

            e(
                message = "JSON parsing error: ${e.message}",
                throwable = e,
                tag = tag
            )
        }
    }

    // ------------------------------------------------------------------------
    // Long Message
    // ------------------------------------------------------------------------

    fun long(
        message: String,
        tag: String = config.globalTag
    ) {
        if (!config.enabled) {
            return
        }

        if (message.length <= MAX_LOG_LENGTH) {
            d(
                message,
                tag
            )
            return
        }

        val total =
            (message.length + MAX_LOG_LENGTH - 1) /
                    MAX_LOG_LENGTH

        var start = 0
        var part = 1

        while (start < message.length) {

            val end =
                minOf(
                    start + MAX_LOG_LENGTH,
                    message.length
                )

            d(
                "Part $part/$total: " +
                        message.substring(
                            start,
                            end
                        ),
                tag
            )

            start = end
            part++
        }
    }

    // ------------------------------------------------------------------------
    // Measure Time
    // ------------------------------------------------------------------------

    /**
     * 使用 nanoTime 测量耗时。
     *
     * System.currentTimeMillis() 会受到系统时间调整影响，
     * nanoTime 更适合测量 duration。
     */
    inline fun <T> measureTime(
        operationName: String,
        tag: String = config.globalTag,
        block: () -> T
    ): T {

        if (!config.enabled) {
            return block()
        }

        val start =
            System.nanoTime()

        return try {

            val result =
                block()

            val duration =
                (System.nanoTime() - start) /
                        1_000_000

            d(
                "$operationName completed in ${duration}ms",
                tag
            )

            result

        } catch (e: Exception) {

            val duration =
                (System.nanoTime() - start) /
                        1_000_000

            e(
                "$operationName failed after ${duration}ms",
                e,
                tag
            )

            throw e
        }
    }

    // ------------------------------------------------------------------------
    // Class Logger
    // ------------------------------------------------------------------------

    fun forClass(
        clazz: Class<*>
    ): ClassLogger {

        return classLoggers.computeIfAbsent(
            clazz
        ) {
            ClassLogger(it)
        }
    }

    class ClassLogger(
        clazz: Class<*>
    ) {

        val tag: String =
            clazz.simpleName.ifEmpty {
                clazz.name.substringAfterLast('.')
            }

        fun verbose(
            message: String
        ) {
            LogUtil.v(
                message,
                tag
            )
        }

        fun debug(
            message: String
        ) {
            LogUtil.d(
                message,
                tag
            )
        }

        fun info(
            message: String
        ) {
            LogUtil.i(
                message,
                tag
            )
        }

        fun warn(
            message: String
        ) {
            LogUtil.w(
                message,
                tag
            )
        }

        fun error(
            message: String,
            throwable: Throwable? = null
        ) {
            LogUtil.e(
                message,
                throwable,
                tag
            )
        }

        fun json(
            jsonStr: String
        ) {
            LogUtil.json(
                jsonStr,
                tag
            )
        }

        fun long(
            message: String
        ) {
            LogUtil.long(
                message,
                tag
            )
        }

        inline fun <T> measureTime(
            operationName: String,
            crossinline block: () -> T
        ): T {
            return LogUtil.measureTime(
                operationName,
                tag
            ) {
                block()
            }
        }
    }

    // ------------------------------------------------------------------------
    // Logger Extension
    // ------------------------------------------------------------------------

    fun <T : Any> T.getLogger(): ClassLogger {
        return forClass(
            this::class.java
        )
    }

    // ------------------------------------------------------------------------
    // Date
    // ------------------------------------------------------------------------

    private fun getCurrentTime(): String {
        return LocalDateTime
            .now()
            .format(
                getDateFormatter()
            )
    }

    // ------------------------------------------------------------------------
    // Method
    // ------------------------------------------------------------------------

    fun getCurrentMethodName(): String {
        return getRealCaller()
            ?.methodName
            ?: UNKNOWN
    }

    fun getCallerMethodName(): String {
        return getRealCaller()
            ?.methodName
            ?: UNKNOWN
    }

    // ------------------------------------------------------------------------
    // Config API
    // ------------------------------------------------------------------------

    fun setEnabled(
        enabled: Boolean
    ) {
        updateConfig {
            copy(
                enabled = enabled
            )
        }
    }

    fun setGlobalTag(
        tag: String
    ) {
        updateConfig {
            copy(
                globalTag = tag
            )
        }
    }

    fun setShowThreadInfo(
        show: Boolean
    ) {
        updateConfig {
            copy(
                showThreadInfo = show
            )
        }
    }

    fun setShowCallerInfo(
        show: Boolean
    ) {
        updateConfig {
            copy(
                showCallerInfo = show
            )
        }
    }

    fun setClickableLinks(
        enabled: Boolean
    ) {
        updateConfig {
            copy(
                clickableLinks = enabled
            )
        }
    }

    fun setDateFormat(
        format: String
    ) {
        updateConfig {
            copy(
                dateFormat = format
            )
        }
    }

    fun configure(
        enabled: Boolean = config.enabled,
        globalTag: String = config.globalTag,
        showThreadInfo: Boolean = config.showThreadInfo,
        showCallerInfo: Boolean = config.showCallerInfo,
        clickableLinks: Boolean = config.clickableLinks,
        dateFormat: String = config.dateFormat
    ) {

        config = Config(
            enabled = enabled,
            globalTag = globalTag,
            showThreadInfo = showThreadInfo,
            showCallerInfo = showCallerInfo,
            clickableLinks = clickableLinks,
            dateFormat = dateFormat
        )

        updateDateFormatter(
            dateFormat
        )
    }

    private inline fun updateConfig(
        block: Config.() -> Config
    ) {
        val oldConfig = config
        val newConfig = oldConfig.block()

        config = newConfig

        if (oldConfig.dateFormat != newConfig.dateFormat) {
            updateDateFormatter(
                newConfig.dateFormat
            )
        }
    }

    private fun updateDateFormatter(
        pattern: String
    ) {
        synchronized(this) {

            if (cachedDateFormat == pattern) {
                return
            }

            cachedDateFormatter =
                createDateFormatter(pattern)

            cachedDateFormat =
                pattern
        }
    }

    // ------------------------------------------------------------------------
    // Stack Trace
    // ------------------------------------------------------------------------

    fun printStackTrace(
        throwable: Throwable? = null,
        tag: String = config.globalTag
    ) {
        if (!config.enabled) {
            return
        }

        val stackTrace =
            throwable?.stackTrace
                ?: Thread.currentThread().stackTrace

        val message =
            formatStackTrace(
                stackTrace = stackTrace,
                clickable = config.clickableLinks,
                title = "Stack trace"
            )

        Log.d(
            tag,
            message
        )
    }

    fun printCallStack(
        maxDepth: Int = 5,
        tag: String = config.globalTag
    ) {
        if (!config.enabled) {
            return
        }

        if (maxDepth <= 0) {
            return
        }

        val stackTrace =
            Thread.currentThread().stackTrace

        val message =
            formatStackTrace(
                stackTrace = stackTrace,
                clickable = config.clickableLinks,
                maxDepth = maxDepth,
                title = "Call stack"
            )

        Log.d(
            tag,
            message
        )
    }

    private fun formatStackTrace(
        stackTrace: Array<StackTraceElement>,
        clickable: Boolean,
        maxDepth: Int = Int.MAX_VALUE,
        title: String
    ): String {

        return buildString {

            append(title)

            if (maxDepth != Int.MAX_VALUE) {
                append(" (max depth: ")
                append(maxDepth)
                append(")")
            }

            append(":\n")

            var count = 0

            for (element in stackTrace) {

                if (isInternalClass(element.className)) {
                    continue
                }

                if (count >= maxDepth) {
                    break
                }

                appendStackElement(
                    element = element,
                    clickable = clickable
                )

                count++
            }
        }
    }

    private fun StringBuilder.appendStackElement(
        element: StackTraceElement,
        clickable: Boolean
    ) {

        if (clickable) {

            append("at ")
            append(element.className)
            append(".")
            append(element.methodName)
            append("(")
            append(
                element.fileName
                    ?: "Unknown Source"
            )
            append(":")
            append(element.lineNumber)
            append(")\n")

        } else {

            val simpleClassName =
                element.className
                    .substringAfterLast('.')

            append("[")
            append(simpleClassName)
            append(".")
            append(element.methodName)
            append(":")
            append(element.lineNumber)
            append("]\n")
        }
    }

    // ------------------------------------------------------------------------
    // Debug State
    // ------------------------------------------------------------------------

    fun isEnabled(): Boolean {
        return config.enabled
    }
}

/**
 * 获取当前对象对应的 Logger。
 */
val <T : Any> T.logger: LogUtil.ClassLogger
    get() = LogUtil.forClass(
        this::class.java
    )

/**
 * Debug 日志。
 */
fun <T : Any> T.debugLog(
    message: String
) {
    LogUtil.forClass(
        this::class.java
    ).debug(message)
}

/**
 * Error 日志。
 */
fun <T : Any> T.errorLog(
    message: String,
    throwable: Throwable? = null
) {
    LogUtil.forClass(
        this::class.java
    ).error(
        message,
        throwable
    )
}

/**
 * 性能统计。
 */
inline fun <T : Any, R> T.measureTime(
    operationName: String,
    crossinline block: () -> R
): R {
    return LogUtil.forClass(
        this::class.java
    ).measureTime(
        operationName
    ) {
        block()
    }
}