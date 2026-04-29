package com.example.spectrplus.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

data class ApiError(val message: String)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatus(ex: ResponseStatusException): ResponseEntity<ApiError> {
        val message = ex.reason ?: ex.message ?: "Ошибка"
        return ResponseEntity.status(ex.statusCode).body(ApiError(message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError(ex.message ?: "Некорректный запрос"))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntime(ex: RuntimeException): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiError(ex.message ?: "Ошибка"))
    }

    @ExceptionHandler(Exception::class)
    fun handleAny(ex: Exception): ResponseEntity<ApiError> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiError(ex.message ?: "Внутренняя ошибка сервера"))
    }
}
