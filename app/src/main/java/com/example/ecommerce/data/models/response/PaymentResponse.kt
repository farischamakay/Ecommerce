package com.example.ecommerce.data.models.response

import com.google.gson.annotations.SerializedName

data class PaymentResponse(

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("data")
    val data: List<PaymentType>,

    @field:SerializedName("message")
    val message: String? = null
)

data class PaymentItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class PaymentType(

    @field:SerializedName("item")
    val item: List<PaymentItem>,

    @field:SerializedName("title")
    val title: String? = null
)
