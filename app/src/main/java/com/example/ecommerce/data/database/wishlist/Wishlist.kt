package com.example.ecommerce.data.database.wishlist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class Wishlist(
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
    val productRating: String?,
    val totalSatisfaction: Int?,
    val productVariant: String,
    val productVariantPrice: Int,
    val isCheck: Boolean = false,
    var quantity: Int = 1,
)
