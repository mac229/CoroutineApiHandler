package com.maciejkozlowski.coroutineapihandler.errors

data class UnknownError(val throwable: Throwable) : ApiError
