package com.example.ecommerce.data.models.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class ProfileRequest(

    @field:SerializedName("userImage")
    val userImage: MultipartBody.Part ?= null,

    @field:SerializedName("userName")
    val userName: MultipartBody.Part,
)
