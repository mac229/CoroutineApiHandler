package com.maciejkozlowski.coroutineapihandler

import okhttp3.Headers
import retrofit2.HttpException

internal fun HttpException.headers(): Headers? {
    return response()?.headers()
}