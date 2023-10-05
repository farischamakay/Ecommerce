package com.example.ecommerce.utils

import com.example.ecommerce.data.models.response.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody

object ErrorMessage {
    fun ResponseBody.getParsingError(): String {
        val responseBody = this.string()
        val errorResponse = Gson().fromJson(
            responseBody,
            ErrorResponse::class.java
        )
        return errorResponse.message
    }
}