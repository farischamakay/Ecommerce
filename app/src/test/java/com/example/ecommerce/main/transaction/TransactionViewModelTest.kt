package com.example.ecommerce.main.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.data.models.response.TransactionDataItem
import com.example.ecommerce.core.data.models.response.TransactionResponse
import com.example.ecommerce.core.data.models.response.TranscationsItem
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.utils.ResourcesResult
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
class TransactionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var productRepository: ProductRepository
    private lateinit var transactionViewModel: TransactionViewModel
    @Before
    fun setUp() {
        productRepository = mock()
        transactionViewModel = TransactionViewModel(productRepository)
    }

    @Test
    fun fetchTransactionTest() = runTest {
        val actualResponse = productRepository.transactions()
        val expectedResponse = TransactionResponse(
            code = 200,
            message= "OK",
            data = listOf(
                TransactionDataItem(
                invoiceId = "invoiceId",
                status =  true,
                date = "date",
                time = "time",
                payment = "payment",
                total= 1000000,
                items = listOf(
                    TranscationsItem(
                        productId = "productId",
                        variantName = "variantName",
                        quantity = 2
                    )
                ),
                rating = 4,
                review =  "review",
                image = "image",
                name= "name"
            )
            )
        )
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
            assertEquals(Unit, transactionViewModel.fetchTransaction())
        }
    }
}