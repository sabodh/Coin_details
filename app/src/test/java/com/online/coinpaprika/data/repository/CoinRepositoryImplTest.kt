package com.online.coinpaprika.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.online.coinpaprika.data.api.ServiceEndPoints
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.Coin
import com.online.coinpaprika.data.model.CoinDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CoinRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var serviceEndPoints: ServiceEndPoints

     private val errorMessage = "server error"

    @Test
    fun `get success coin list from server`() = runTest {
       Mockito.`when`(serviceEndPoints.getCoins())
           .thenReturn(Response.success(Coin()))
        val repository = CoinRepositoryImpl(serviceEndPoints)
        val flow  = repository.getCoins()
        flow.collect { result ->
            assertNotNull(result)
            assertEquals(0, (result as ServiceResponse.Success).data.size)
        }
    }

    @Test
    fun `get success coin details from server`() = runTest {
        Mockito.`when`(serviceEndPoints.getCoinDetails("btc-bitcoin"))
            .thenReturn(Response.success(CoinDetails.default))
        val repository = CoinRepositoryImpl(serviceEndPoints)
        val flow  = repository.getCoinDetails("btc-bitcoin")
        flow.collect { result ->
            assertNotNull(result)
            assertEquals(CoinDetails.default, (result as ServiceResponse.Success).data)
        }
    }

    // HTTP error cases
    @Test
    fun `get 500 error coin list from server`() = runTest {

        val responseBody = errorMessage.toResponseBody(null)
        Mockito.`when`(serviceEndPoints.getCoins())
            .thenReturn(Response.error(500, responseBody))
        val repository = CoinRepositoryImpl(serviceEndPoints)
        val flow  = repository.getCoins()
        flow.collect { result ->
            assertEquals(errorMessage, (result as ServiceResponse.Error).message)
        }
    }

    @Test
    fun `get 500 error coin details from server`() = runTest {
        val responseBody = errorMessage.toResponseBody(null)
        Mockito.`when`(serviceEndPoints.getCoinDetails("btc-bitcoin"))
            .thenReturn(Response.error(500, responseBody))
        val repository = CoinRepositoryImpl(serviceEndPoints)
        val flow  = repository.getCoinDetails("btc-bitcoin")
        flow.collect { result ->
            assertEquals(errorMessage, (result as ServiceResponse.Error).message)
        }
    }


}