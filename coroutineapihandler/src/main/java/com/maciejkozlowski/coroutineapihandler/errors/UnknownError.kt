package com.maciejkozlowski.coroutineapihandler.errors

class UnknownError(errorResponse: ErrorResponse) : ApiError(errorResponse)