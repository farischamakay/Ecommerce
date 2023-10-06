package com.example.ecommerce.data.network

import com.example.ecommerce.core.data.network.UserApiService
import com.example.ecommerce.data.models.request.RefreshRequest
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.LoginData
import com.example.ecommerce.data.models.response.LoginResponse
import com.example.ecommerce.data.models.response.ProfileData
import com.example.ecommerce.data.models.response.ProfileResponse
import com.example.ecommerce.data.models.response.RefreshData
import com.example.ecommerce.data.models.response.RefreshResponse
import com.example.ecommerce.data.models.response.RegisterData
import com.example.ecommerce.data.models.response.RegisterResponse
import com.example.ecommerce.utils.JsonConverter.q
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals


@RunWith(JUnit4::class)
class UserApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var userApiService: UserApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        userApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)

    }

    @After
    fun finish(){
        mockWebServer.shutdown()
    }

    @Test
    fun registerTest() = runTest{
        mockWebServer.q("register.json", 200)

            val actualResponse = userApiService.register(
                UserRequest(password = null, firebaseToken = "", email = null))

            val dataExpected = RegisterResponse(
                code = 200,
                message = "OK",
                data = RegisterData(
                    accessToken = "accessToken",
                    refreshToken = "refreshToken",
                    expiresAt = 600
                )
            )
            assertEquals(dataExpected, actualResponse.body())
        }

    @Test
    fun loginTest() = runTest{
        mockWebServer.q("login.json", 200)

        val actualResponse = userApiService.login(userRequest = UserRequest(password = null,
            firebaseToken = "", email = null)
        )

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
        assertEquals(actualResponse.body(), expectedResponse)
    }

    @Test
    fun profileTest() = runTest {
        mockWebServer.q("profile.json", 200)

        val user = "abcde"
        val image = "http:/image"

        val actualResponse = userApiService.updateProfile(
            MultipartBody.Part.createFormData("username", user),
            MultipartBody.Part.createFormData("image", image))

        val expectedResponse = ProfileResponse(
            code = 200,
            message = "OK",
            data = ProfileData(
                userName = "userName",
                userImage = "userImage"
            )
        )
        assertEquals(actualResponse.body(),expectedResponse)
    }

    @Test
    fun refreshTest() = runTest {
        mockWebServer.q("refresh.json", 200)

        val expectedResponse = userApiService.refresh(token = RefreshRequest("abcd"))

        val actualResponse = RefreshResponse(
            code = 200,
            message = "OK",
            data = RefreshData(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )
        assertEquals(expectedResponse.body(),actualResponse)
    }
}