package com.maciejkozlowski.coroutineapihandler

import retrofit2.HttpException
import java.io.IOException

abstract class BaseApiRequestHandler<ERROR> {

    suspend fun <RESULT, DATA> handleCompletableRequest(
        request: suspend () -> DATA,
        onCompleted: () -> RESULT,
        onError: (ERROR) -> RESULT,
    ): RESULT =
        try {
            request()
            onCompleted()
        } catch (throwable: Throwable) {
            onError(
                mapToApiError(throwable)
            )
        }

    suspend fun <RESULT, DATA> handleRequest(
        request: suspend () -> DATA,
        onSuccess: (DATA) -> RESULT,
        onError: (ERROR) -> RESULT,
    ): RESULT =
        try {
            onSuccess(request())
        } catch (throwable: Throwable) {
            onError(
                mapToApiError(throwable)
            )
        }

    private fun mapToApiError(throwable: Throwable): ERROR =
        when (throwable) {
            is HttpException -> createRequestError(throwable)
            is IOException   -> createConnectionError()
            else             -> createUnknownError(throwable)
        }

    protected abstract fun createConnectionError(): ERROR

    protected abstract fun createRequestError(exception: HttpException): ERROR

    protected abstract fun createUnknownError(throwable: Throwable): ERROR
}