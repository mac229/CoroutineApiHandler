package com.maciejkozlowski.coroutineapihandler

import com.maciejkozlowski.coroutineapihandler.errors.*
import com.maciejkozlowski.coroutineapihandler.reponse.ApiResponse
import com.maciejkozlowski.coroutineapihandler.reponse.CompletableApiResponse
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

/**
 * Created by Maciej Kozłowski on 2019-10-23.
 */

abstract class BaseApiRequestHandler<T>(
        private val parser: Parser<T>,
        private val logger: ApiLogger? = null
) {

    suspend fun <T> handleCompletableRequest(request: suspend () -> T): CompletableApiResponse {
        return try {
            request()
            CompletableApiResponse.Complete
        } catch (throwable: Throwable) {
            logger?.logError(throwable)
            CompletableApiResponse.Error(mapToApiError(throwable))
        }
    }

    suspend fun <T> handleRequest(request: suspend () -> T): ApiResponse<T> {
        return try {
            ApiResponse.Success(request())
        } catch (throwable: Throwable) {
            logger?.logError(throwable)
            ApiResponse.Error(mapToApiError(throwable))
        }
    }

    private fun mapToApiError(throwable: Throwable): ApiError {
        return when (throwable) {
            is HttpException -> handleHttpException(throwable)
            is IOException   -> ConnectionError(ErrorResponse(throwable))
            else             -> UnknownError(ErrorResponse(throwable))
        }
    }

    private fun handleHttpException(exception: HttpException): ApiError {
        return when (exception.code()) {
            in (400 until 500) -> handleBadRequest(exception)
            in (500 until 600) -> ServerError(ErrorResponse(exception, exception.headers()))
            else               -> UnknownError(ErrorResponse(exception))
        }
    }

    private fun handleBadRequest(exception: HttpException): ApiError {

        return when (exception.code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> UnauthorizedError(ErrorResponse(exception, exception.headers()))
            else                                -> parseBadRequest(exception)
        }
    }

    private fun parseBadRequest(exception: HttpException): ApiError {
        val errors = parseErrorBodySafely(exception.response()?.errorBody())
        return if (errors != null) {
            createRequestError(errors)
        } else {
            UnknownError(ErrorResponse(exception))
        }
    }

    private fun parseErrorBodySafely(errorBody: ResponseBody?): T? {
        return errorBody?.string()?.let(this::parseErrors)
    }

    protected abstract fun createRequestError(errors: T): ApiError

    private fun parseErrors(json: String): T? {
        return try {
            parser.parseFromJson(json)
        } catch (exception: Exception) {
            null
        }
    }
}