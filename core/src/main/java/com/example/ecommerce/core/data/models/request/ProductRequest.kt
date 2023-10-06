package com.example.ecommerce.core.data.models.request

data class ProductRequest(
    val search: String? = null,
    val brand: String? = null,
    val lowest: Int? = null,
    val highest: Int? = null,
    val sort: String? = null,
)