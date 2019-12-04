package com.maciejkozlowski.coroutineapihandler

/**
 * Created by Maciej Kozłowski on 2019-12-04.
 */

interface ApiLogger {

    fun logError(throwable: Throwable)
}