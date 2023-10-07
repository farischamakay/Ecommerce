package com.example.ecommerce.data.repository

import com.example.ecommerce.core.data.models.request.UserRequest
import com.example.ecommerce.core.data.models.response.LoginData
import com.example.ecommerce.core.data.models.response.LoginResponse
import com.example.ecommerce.core.data.models.response.RegisterData
import com.example.ecommerce.core.data.models.response.RegisterResponse
import com.example.ecommerce.core.data.network.UserApiService
import com.example.ecommerce.utils.ResourcesResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response


@RunWith(JUnit4::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userApiService: UserApiService

    @Before
    fun setup(){
        userApiService = mock()
        userRepository = UserRepository(userApiService)
    }

    @Test
    fun loginUserTest() = runTest{
        val actualResponse = userApiService
            .login(userRequest = UserRequest(password = null, firebaseToken = "", email = null))

        val expectedResponse = LoginResponse(
            code = 200,
            message = "OK",
            data = LoginData(
                userName = "userName",
                userImage = "userImage",
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = userRepository.loginUser(userRequest = UserRequest(password = null, firebaseToken = "", email = null))
        assertEquals((result as ResourcesResult.Success).data,expectedResponse)
    }

    @Test
    fun registerUser() = runTest{
        val actualResponse = userApiService
            .register(userRequest = UserRequest(password = null, firebaseToken = "", email = null))

        val expectedResponse = RegisterResponse(
            code = 200,
            message = "OK",
            data = RegisterData(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = userRepository.registerUser(userRequest = UserRequest(password = null, firebaseToken = "", email = null))
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

}