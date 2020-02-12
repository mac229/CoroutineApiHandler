package com.maciejkozlowski.coroutineapihandler.errors

class ServerError(errorResponse: ErrorResponse) : ApiError(errorResponse)