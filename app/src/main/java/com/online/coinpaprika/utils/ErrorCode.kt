package com.online.coinpaprika.utils

enum class ErrorCode(val statusCode: Int) {

    NETWORK_ERROR(404),
    UNKNOWN_ERROR(405),
    SERVER_ERROR(500),
    EXCEPTION(600)
}

