package com.spundev.websearchtextaction.util

import java.net.URL

/**
 * Check if the url is a valid url
 * Before this, we were using Patterns.WEB_URL, and it worked fine but failed with uncommon urls like
 * http://localhost. This is more flexible but fails for urls without scheme.
 */
internal fun isValidUrl(url: String): Boolean {
    return try {
        // Require a host so a scheme-only URL (e.g., "http://") isn't valid.
        URL(url).host.isNotEmpty()
    } catch (_: Exception) {
        false
    }
}
