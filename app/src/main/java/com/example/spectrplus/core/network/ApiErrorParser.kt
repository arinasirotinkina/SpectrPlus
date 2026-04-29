package com.example.spectrplus.core.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.IOException

private data class ApiErrorDto(val message: String?)

private val gson = Gson()

fun parseApiError(throwable: Throwable): String {
    return when (throwable) {
        is HttpException -> {
            val body = runCatching { throwable.response()?.errorBody()?.string() }.getOrNull()
            val parsed = body?.let {
                runCatching { gson.fromJson(it, ApiErrorDto::class.java) }.getOrNull()
            }
            parsed?.message?.takeIf { it.isNotBlank() }
                ?: defaultHttpMessage(throwable.code())
        }
        is IOException -> "Нет соединения с сервером. Проверьте интернет."
        else -> throwable.message?.takeIf { it.isNotBlank() } ?: "Неизвестная ошибка"
    }
}

private fun defaultHttpMessage(code: Int): String = when (code) {
    400 -> "Некорректный запрос"
    401 -> "Неверный email или пароль"
    403 -> "Доступ запрещён"
    404 -> "Ресурс не найден"
    409 -> "Конфликт данных"
    500, 502, 503 -> "Сервер временно недоступен"
    else -> "Ошибка ($code)"
}
