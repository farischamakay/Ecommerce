package com.example.ecommerce.core.data.models.response

import com.google.gson.annotations.SerializedName

data class SearchResponse<T>(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: T,

    @field:SerializedName("message")
    val message: String? = null
)
