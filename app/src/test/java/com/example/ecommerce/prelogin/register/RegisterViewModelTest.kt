package com.example.ecommerce.prelogin.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.RegisterData
import com.example.ecommerce.data.models.response.RegisterResponse
import com.example.ecommerce.data.repository.UserRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.MainDispatcherRule
import com.example.ecommerce.utils.ResourcesResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var userRepository: UserRepository
    private lateinit var sharedPreference : PreferenceProvider
    private lateinit var viewModel: RegisterViewModel
    @Before
    fun setUp() {
        userRepository = mock()
        sharedPreference = mock()
        viewModel = RegisterViewModel(userRepository,sharedPreference)
    }

    @Test
    fun registerUser() = runTest{
        val actualResponse = userRepository.registerUser(userRequest = UserRequest(password = null,
            firebaseToken = "", email = null))
        val expectedResponse = RegisterResponse(
            code = 200,
            message = "OK",
            data = RegisterData(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresAt = 600
            )
        )
        whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
        launch {
            viewModel.registerUser(userRequest = UserRequest(password = null,
                firebaseToken = "", email = null))
        }.join()
        //assertEquals(ResourcesResult.Loading, loginViewModel.loginResult.value)
        advanceTimeBy(1)
        assertEquals(ResourcesResult.Success(expectedResponse), viewModel.registerResult.value)
    }

    @Test
    fun saveToken() = runTest{
        val accessToken = "yourAccessToken"
        val refreshToken = "yourRefreshToken"
        doNothing().whenever(sharedPreference).saveAccess(accessToken, refreshToken)
        viewModel.saveToken(accessToken, refreshToken)
        verify(sharedPreference).saveAccess(accessToken, refreshToken)
    }

    @Test
    fun registerErrorTest() = runTest {
        val actualResponse = userRepository.registerUser(userRequest = UserRequest(password = null,
            firebaseToken = "", email = null))
        whenever(actualResponse).thenReturn(ResourcesResult.Failure("error"))
        launch {
            viewModel.registerUser(userRequest = UserRequest(password = null,
                firebaseToken = "", email = null))
        }.join()
        //assertEquals(ResourcesResult.Loading, loginViewModel.loginResult.value)
        advanceTimeBy(1)
        assertEquals(ResourcesResult.Failure("error"), viewModel.registerResult.value)
    }
}