package com.maciejkozlowski.coroutineapihandler.errors

import com.maciejkozlowski.coroutineapihandler.Headers


data class ErrorResponse(
        val throwable: Throwable? = null,
        val headers: Headers? = null
) {
    internal constructor(throwable: Throwable?, headers: okhttp3.Headers?)
            : this(throwable, headers?.let { Headers(it) })
}