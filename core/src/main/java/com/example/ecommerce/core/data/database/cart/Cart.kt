package com.example.ecommerce.core.data.database.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey val productId: String,
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
)