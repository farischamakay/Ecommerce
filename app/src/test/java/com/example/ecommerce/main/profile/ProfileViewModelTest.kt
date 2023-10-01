package com.example.ecommerce.main.profile


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.data.models.request.ProfileRequest
import com.example.ecommerce.data.models.response.ProfileData
import com.example.ecommerce.data.models.response.ProfileResponse
import com.example.ecommerce.data.repository.UserRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.MainDispatcherRule
import com.example.ecommerce.utils.ResourcesResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ProfileViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var userRepository: UserRepository
    private lateinit var sharedPreferences: PreferenceProvider
    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setUp() {
        userRepository = mock()
        sharedPreferences = mock()
        profileViewModel = ProfileViewModel(userRepository, sharedPreferences)
    }

    @Test
    fun updateProfile() = runTest {
        val user = "abcde"
        val image = "http:/image"
        val actualResponse = userRepository.profileUser(profileRequest = ProfileRequest(
            MultipartBody.Part.createFormData("username", user),
            MultipartBody.Part.createFormData("image", image)))
        val expectedResponse = ProfileResponse(
            code = 200,
            message = "OK",
            data = ProfileData(
                userName = "userName",
                userImage = "userImage"
            )
        )
        whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
        backgroundScope.launch {
            profileViewModel.updateProfile(profileRequest = ProfileRequest(
                MultipartBody.Part.createFormData("username", user),
                MultipartBody.Part.createFormData("image", image)))
            assertEquals(ResourcesResult.Success(expectedResponse), profileViewModel.profileResult.value)
        }
    }

    @Test
    fun saveUserName() {
        doNothing().whenever(sharedPreferences).saveUsername("Doi")
        assertEquals(Unit, profileViewModel.saveUserName("Doi"))
    }
}