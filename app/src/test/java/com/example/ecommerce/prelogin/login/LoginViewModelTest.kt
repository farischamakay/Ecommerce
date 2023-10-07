package com.example.ecommerce.prelogin.login


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.data.models.request.UserRequest
import com.example.ecommerce.core.data.models.response.LoginData
import com.example.ecommerce.core.data.models.response.LoginResponse
import com.example.ecommerce.data.repository.UserRepository
import com.example.ecommerce.core.data.preferences.PreferenceProvider
import com.example.ecommerce.utils.MainDispatcherRule
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.observeForTesting
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
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.lang.RuntimeException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var userRepository: UserRepository
    private lateinit var sharedPreferences: PreferenceProvider
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        userRepository = mock()
        sharedPreferences = mock()
        loginViewModel = LoginViewModel(userRepository, sharedPreferences)
    }

    @Test
    fun getLoginResult() = runTest {
        val actualResponse = sharedPreferences.isOnBoardingCompleted()
        assertEquals(false, actualResponse)
    }

    @Test
    fun loginUser() = runTest {
        val actualResponse = userRepository.loginUser(userRequest = UserRequest(password = null,
            firebaseToken = "", email = null))

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
        whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
        launch {
            loginViewModel.loginUser(userRequest = UserRequest(password = null,
                firebaseToken = "", email = null)
            )
        }.join()
        advanceTimeBy(1)
        assertEquals(ResourcesResult.Success(expectedResponse), loginViewModel.loginResult.value)
    }

    @Test
    fun saveToken() {
        doNothing().whenever(sharedPreferences).saveAccess(anyString(), anyString())
        loginViewModel.saveToken("123", "456")
        verify(sharedPreferences).saveAccess("123", "456")
    }

    @Test
    fun saveUserName() {
        doNothing().whenever(sharedPreferences).saveUsername(anyString())
        loginViewModel.saveUserName("Doi")
        verify(sharedPreferences).saveUsername("Doi")
    }

    @Test
    fun getUsername() {
        whenever(sharedPreferences.getUsername()).thenReturn("Doi")
        val result = loginViewModel.getUsername()
        assertEquals("Doi", result)
    }

    @Test
    fun loginErrorTest() = runTest {
        val actualResponse = userRepository.loginUser(userRequest = UserRequest(password = null,
            firebaseToken = "", email = null))
        val expectedError = RuntimeException("error")
        whenever(actualResponse).thenThrow(expectedError)
        backgroundScope.launch {
            loginViewModel.loginUser(userRequest = UserRequest(password = null,
                firebaseToken = "", email = null))
            val actualReturn = loginViewModel.loginResult
            actualReturn.observeForTesting {
                assertEquals(expectedError.message, (actualReturn.value as ResourcesResult.Failure).error)
            }
        }
    }
}