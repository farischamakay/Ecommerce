package com.example.ecommerce.data.repository


import com.example.ecommerce.core.data.models.request.ProfileRequest
import com.example.ecommerce.core.data.models.request.UserRequest
import com.example.ecommerce.core.data.models.response.LoginResponse
import com.example.ecommerce.core.data.models.response.ProfileResponse
import com.example.ecommerce.core.data.models.response.RegisterResponse
import com.example.ecommerce.core.data.network.UserApiService
import com.example.ecommerce.utils.ErrorMessage.getParsingError
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
                ResourcesResult.Failure(response.errorBody()?.getParsingError())
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
                ResourcesResult.Failure(response.errorBody()?.getParsingError())
            }
        } catch (e: Exception) {
            ResourcesResult.Failure("Exception : ${e.message}")
        }
    }

    suspend fun profileUser(profileRequest: ProfileRequest?): ResourcesResult<ProfileResponse> {
        return try {
            val response = userApiService.updateProfile(
                userName = profileRequest?.userName,
                userImage = profileRequest?.userImage
            )
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    ResourcesResult.Success(data)
                } else {
                    ResourcesResult.Failure("Response Body is null")
                }
            } else {
                ResourcesResult.Failure(response.errorBody()?.getParsingError())
            }
        } catch (e: Exception) {
            ResourcesResult.Failure("Exception : ${e.message}")
        }
    }
}