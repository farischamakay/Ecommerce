package com.example.ecommerce.data.models.request

import com.google.gson.annotations.SerializedName

data class FullfilmentRequest(

    @field:SerializedName("payment")
    val payment: String? = null,

    @field:SerializedName("items")
    val items: List<FullfilmentItem>? = null
)

data class FullfilmentItem(

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("productId")
    val productId: String? = null,

    @field:SerializedName("variantName")
    val variantName: String? = null
)
