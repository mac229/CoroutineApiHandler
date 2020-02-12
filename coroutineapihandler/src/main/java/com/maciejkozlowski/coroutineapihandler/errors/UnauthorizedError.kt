package com.maciejkozlowski.coroutineapihandler.errors

class UnauthorizedError(errorResponse: ErrorResponse) : ApiError(errorResponse)