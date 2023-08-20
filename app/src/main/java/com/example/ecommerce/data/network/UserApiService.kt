package com.example.ecommerce.data.network

import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.LoginResponse
import com.example.ecommerce.data.models.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("login")
    suspend fun login(
        @Body userRequest: UserRequest
    ) : Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body userRequest: UserRequest
    ) : Response<RegisterResponse>

}