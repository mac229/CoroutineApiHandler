package com.maciejkozlowski.coroutineapihandler.errors

/**
 * Created by Maciej Kozłowski on 2019-04-30.
 */
abstract class ApiError(val errorResponse: ErrorResponse? = null)