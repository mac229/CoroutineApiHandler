package com.maciejkozlowski.coroutineapihandler

import okhttp3.Headers

class Headers(
        private val headers: Headers
) {

    /**
     * @return The last value corresponding to the specified field, or null.
     */
    operator fun get(name: String): String? {
        return headers[name]
    }

    fun toMultiMap(): Map<String, List<String>> {
        return headers.toMultimap()
    }
}