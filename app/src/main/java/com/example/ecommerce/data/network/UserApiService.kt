package com.example.ecommerce.data.network

import com.example.ecommerce.data.models.request.RefreshRequest
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.LoginResponse
import com.example.ecommerce.data.models.response.ProfileResponse
import com.example.ecommerce.data.models.response.RefreshResponse
import com.example.ecommerce.data.models.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserApiService {

    @POST("login")
    suspend fun login(
        @Body userRequest: UserRequest
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body userRequest: UserRequest
    ): Response<RegisterResponse>

    @POST("refresh")
    suspend fun refresh(
        @Body token : RefreshRequest?
    ) : Response<RefreshResponse>

    @Multipart
    @POST("/profile")
    suspend fun updateProfile(
        @Part userName: MultipartBody.Part,
        @Part userImage: MultipartBody.Part
    ): Response<ProfileResponse>

}