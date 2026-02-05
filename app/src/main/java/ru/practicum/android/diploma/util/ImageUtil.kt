package ru.practicum.android.diploma.util

object ImageUtil {

    fun normalizeLogoUrl(url: String?): String? {
        if (url.isNullOrBlank()) return null

        return when {
            url.endsWith(".png", ignoreCase = true) -> url

            url.endsWith(".svg", ignoreCase = true) -> null

            else -> url
        }
    }
}
