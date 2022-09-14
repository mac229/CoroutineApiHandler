package com.maciejkozlowski.coroutineapihandler

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

/**
 * Created by Maciej Kozłowski on 2019-10-23.
 */

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
            is HttpException -> handleHttpException(throwable)
            is IOException   -> createConnectionError()
            else             -> createUnknownError(throwable)
        }

    private fun handleHttpException(exception: HttpException): ERROR =
        when (exception.code()) {
            in (400 until 500) -> handleBadRequest(exception)
            in (500 until 600) -> createServerError()
            else               -> createUnknownError(exception)
        }

    private fun handleBadRequest(exception: HttpException): ERROR =
        when (exception.code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> createUnauthorizedError()
            else                                -> createRequestError(exception)
        }

    protected abstract fun createUnauthorizedError(): ERROR

    protected abstract fun createConnectionError(): ERROR

    protected abstract fun createRequestError(exception: HttpException): ERROR

    protected abstract fun createServerError(): ERROR

    protected abstract fun createUnknownError(throwable: Throwable): ERROR
}