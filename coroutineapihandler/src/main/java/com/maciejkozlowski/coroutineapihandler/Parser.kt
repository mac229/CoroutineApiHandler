package com.maciejkozlowski.coroutineapihandler

/**
 * Created by Maciej Kozłowski on 2019-04-29.
 */
interface Parser<T> {

    fun parseFromJson(json: String) : T?
}