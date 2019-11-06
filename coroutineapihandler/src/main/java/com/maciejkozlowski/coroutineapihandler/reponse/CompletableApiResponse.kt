package com.maciejkozlowski.coroutineapihandler.reponse

import com.maciejkozlowski.coroutineapihandler.errors.ApiError

/**
 * Created by Maciej Kozłowski on 2019-10-23.
 */

sealed class CompletableApiResponse {

    object Complete : CompletableApiResponse()
    data class Error(val error: ApiError) : CompletableApiResponse()
}