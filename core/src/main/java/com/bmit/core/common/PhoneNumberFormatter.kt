package com.bmit.core.common

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

object PhoneNumberFormatter {

    private val phoneUtil = PhoneNumberUtil.getInstance()

    enum class Format {
        INTERNATIONAL, // +86 138 0013 8000
        NATIONAL,      // 138 0013 8000
        E164,          // +8613800138000
        RFC3966        // tel:+86-138-0013-8000
    }

    /**
     * 格式化电话号码。
     *
     * @param number 原始号码
     * @param region 号码所属地区，例如 CN、SG、US。
     *               国际号码以 + 开头时可以不传。
     * @param format 目标格式
     * @return 格式化号码；解析失败时返回 null
     */
    fun format(
        number: String?,
        region: String? = null,
        format: Format = Format.INTERNATIONAL
    ): String? {
        val input = number?.trim().orEmpty()
        if (input.isEmpty()) return null

        val defaultRegion = if (input.startsWith("+")) {
            "ZZ"
        } else {
            region?.trim()
                ?.uppercase(Locale.ROOT)
                ?.takeIf { it.matches(Regex("[A-Z]{2}")) }
                ?: return null
        }

        return try {
            val parsed = phoneUtil.parse(input, defaultRegion)

            val phoneFormat = when (format) {
                Format.INTERNATIONAL ->
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL

                Format.NATIONAL ->
                    PhoneNumberUtil.PhoneNumberFormat.NATIONAL

                Format.E164 ->
                    PhoneNumberUtil.PhoneNumberFormat.E164

                Format.RFC3966 ->
                    PhoneNumberUtil.PhoneNumberFormat.RFC3966
            }

            // E.164 只输出通过有效性校验的号码，避免生成错误的标准值。
            if (format == Format.E164 &&
                !phoneUtil.isValidNumber(parsed)
            ) {
                return null
            }

            phoneUtil.format(parsed, phoneFormat)
        } catch (_: NumberParseException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}