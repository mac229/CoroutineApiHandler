package com.maciejkozlowski.coroutineapihandler.reponse

import com.maciejkozlowski.coroutineapihandler.errors.ApiError

/**
 * Created by Maciej Kozłowski on 2019-10-23.
 */

sealed class ApiResponse<T> {

    data class Success<T>(val data: T) : ApiResponse<T>()
    data class Error<T>(val error: ApiError) : ApiResponse<T>()
}