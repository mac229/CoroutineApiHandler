package com.maciejkozlowski.coroutineapihandler.errors

class ConnectionError(errorResponse: ErrorResponse) : ApiError(errorResponse)