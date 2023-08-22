package com.example.ecommerce.data.repository

import android.util.Log
import com.example.ecommerce.data.models.request.ProfileRequest
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.LoginResponse
import com.example.ecommerce.data.models.response.ProfileData
import com.example.ecommerce.data.models.response.ProfileResponse
import com.example.ecommerce.data.models.response.RegisterResponse
import com.example.ecommerce.data.network.UserApiService
import com.example.ecommerce.utils.ResourcesResult
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApiService: UserApiService) {

    suspend fun loginUser(userRequest: UserRequest): ResourcesResult<LoginResponse> {
        return try {
            val response = userApiService.login(userRequest)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ResourcesResult.Success(data)
                } else {
                    ResourcesResult.Failure("Response body is null")
                }
            } else {
                ResourcesResult.Failure("Error response: ${response.code()} -> ${response.message()}")
            }
        } catch (e: Exception) {
            ResourcesResult.Failure("Exception: ${e.message}")
        }
    }

    suspend fun registerUser(userRequest: UserRequest): ResourcesResult<RegisterResponse> {
        return try {
            val response = userApiService.register(userRequest)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ResourcesResult.Success(data)
                } else {
                    ResourcesResult.Failure("Response Body is null")
                }
            } else {
                ResourcesResult.Failure("Error response: ${response.code()} -> ${response.message()}")
            }
        } catch (e: Exception) {
            ResourcesResult.Failure("Exception : ${e.message}")
        }
    }

    suspend fun profileUser(profileRequest: ProfileRequest) : ResourcesResult<ProfileResponse> {
        return try {
            val response = userApiService.updateProfile(userName = profileRequest.userName, userImage = profileRequest.userImage)
            if(response.isSuccessful){
                val data = response.body()
                if(data != null) {
                    ResourcesResult.Success(data)
                } else {
                    ResourcesResult.Failure("Response Body is null")
                }
            } else {
                ResourcesResult.Failure("Error response: ${response.code()} -> ${response.message()}")
            }
        } catch (e : Exception){
            ResourcesResult.Failure("Exception : ${e.message}")
        }
    }
}