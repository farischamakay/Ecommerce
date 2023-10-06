package com.example.ecommerce.core.data.models.request

import com.google.gson.annotations.SerializedName

data class RatingRequest(

    @field:SerializedName("review")
    val review: String? = null,

    @field:SerializedName("rating")
    val rating: Int? = null,

    @field:SerializedName("invoiceId")
    val invoiceId: String? = null
)
