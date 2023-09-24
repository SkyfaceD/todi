package org.skyfaced.noti.settings

import org.skyfaced.noti.util.exception.EnumInferenceException

/**
 * @param tag contains string representation of ISO 639-1 code in lower case
 */
enum class NotiLocale(
    val tag: String
) {
    English("en"),
    Russian("ru"),
    ;

    companion object {
        fun from(tag: String) = when (tag.lowercase()) {
            "en" -> English
            "ru" -> Russian
            else -> throw EnumInferenceException("Can't define locale by passed tag: $tag")
        }

        fun from(ordinal: Int) = entries.getOrNull(ordinal)
            ?: throw EnumInferenceException("Can't define locale by passed ordinal: $ordinal")
    }
}