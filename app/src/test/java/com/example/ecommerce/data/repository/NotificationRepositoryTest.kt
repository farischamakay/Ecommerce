package com.example.ecommerce.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.core.data.database.notification.NotificationDao
import com.example.ecommerce.utils.getOrAwaitValue
import com.example.ecommerce.utils.liveDataOf
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
class NotificationRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var notificationDao: NotificationDao

    private val notification = Notification(
        id = 1,
        title = "Promo",
        body = "Promo akhir tahunan di bulan September",
        image = "https::/image",
        type = "Promo",
        date = "11/20/23",
        time = "13.30",
        isSelected = true
    )

    @Before
    fun setUp() {
        notificationDao = mock()
        notificationRepository = NotificationRepository(notificationDao)
    }
    @Test
    fun fetchDataNotification() = runTest {
        val expectedResponse = notification
        whenever(notificationDao.getAll()).thenReturn(liveDataOf(listOf(expectedResponse)))
        val result = notificationRepository.fetchDataNotification().getOrAwaitValue()
        assertEquals(listOf(expectedResponse),result)
    }

    @Test
    fun insertDataNotification() = runTest {
        val actualResponse = notificationDao.getAll()
        val expectedResponse = notification
        whenever(actualResponse).thenReturn(liveDataOf(listOf(expectedResponse)))
        val result = notificationRepository.insertDataNotification(expectedResponse)
        assertEquals(Unit, result)
    }

    @Test
    fun updateValues() = runTest {
        val expectedResponse = notification
        whenever(notificationDao.updateNotification(notification.copy(title = "abcde"))).thenReturn(Unit)
        val result = notificationRepository.updateValues(expectedResponse.copy(title = "abcde"))
        assertEquals(Unit, result)
    }
}