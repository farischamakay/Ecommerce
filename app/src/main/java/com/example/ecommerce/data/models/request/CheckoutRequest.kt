package com.example.ecommerce.data.models.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class CheckoutRequest (
    val productId: String,
    val productName: String?,
    val image: String?,
    val brand: String?,
    val description: String?,
    val store: String?,
    val sale: String?,
    val stock: Int?,
    val totalRating: Int?,
    val totalReview: Int?,
    val totalSatisfaction: Int?,
    val productVariant: String,
    val productVariantPrice: Int,
    val isCheck: Boolean = false,
    var quantity: Int = 1,
) : Parcelable