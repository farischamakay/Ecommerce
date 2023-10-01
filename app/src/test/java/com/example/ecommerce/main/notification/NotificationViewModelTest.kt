package com.example.ecommerce.main.notification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.data.database.notification.Notification
import com.example.ecommerce.data.repository.NotificationRepository
import com.example.ecommerce.utils.getOrAwaitValue
import com.example.ecommerce.utils.liveDataOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class NotificationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var notificationViewModel: NotificationViewModel

    private val notification = Notification(1,
        "Promo Telkomsel",
        "Promo tahunan hari ini sebesar 1.000 per item",
        "https:image",
        "Promo",
        "24/03/23",
        "14.30")
    @Before
    fun setUp() {
        notificationRepository = mock()
    }
    @Test
    fun getGetDataNotification() = runTest{
        val actualResponse = notification
        whenever(notificationRepository.fetchDataNotification()).thenReturn(liveDataOf(listOf(actualResponse)))
        notificationViewModel = NotificationViewModel(notificationRepository)
        assertEquals(listOf(actualResponse), notificationViewModel.getDataNotification.getOrAwaitValue())
    }
    @Test
    fun updateNotification() = runTest{
        val actualResponse = notificationRepository.updateValues(notification.copy(isSelected = true))
        whenever(actualResponse).thenReturn(Unit)
        backgroundScope.launch {
            notificationViewModel = NotificationViewModel(notificationRepository)
            assertEquals(Unit, notificationViewModel.updateNotification(notification.copy(isSelected = true)))
        }
    }
}