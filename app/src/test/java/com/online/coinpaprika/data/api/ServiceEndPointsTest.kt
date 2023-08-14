package com.online.coinpaprika.data.api

import com.online.coinpaprika.utils.Helper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@OptIn(ExperimentalCoroutinesApi::class)
class ServiceEndPointsTest {

    private lateinit var mockServer: MockWebServer
    private lateinit var serviceEndPoints: ServiceEndPoints

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        serviceEndPoints = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ServiceEndPoints::class.java)
    }

    @Test
    fun `get success coin list from server`() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/Coins.json")
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody(content)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
        assertEquals(4, response.body()?.size)
    }

    @Test
    fun `get success coin empty list from server`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody("[]")
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
        assertTrue(response.body()!!.isEmpty())
        assertEquals(0, response.body()?.size)
    }

    @Test
    fun `get success coin details from server`() = runTest {
        val mockResponse = MockResponse()
        val content = Helper.readFileResource("/CoinDetails.json")
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody(content)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoinDetails("btc-bitcoin")
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
    }

    @Test
    fun `get success coin empty details from server`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
        mockResponse.setBody("{}")
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoinDetails("btc-bitcoins")
        mockServer.takeRequest()
        assertEquals(true, response.isSuccessful)
        assertNotNull(response.body())
        assertNull(response.errorBody())
    }

    // HTTP error cases
    @Test
    fun `get 500 error coin list from server`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(false, response.isSuccessful)
        assertNotNull(response.errorBody())
        assertEquals(response.code(), HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertEquals(response.message(), "Server Error")
    }

    @Test
    fun `get 500 error coin details from server`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoinDetails("btc-bitcoin")
        mockServer.takeRequest()
        assertEquals(false, response.isSuccessful)
        assertNotNull(response.errorBody())
        assertEquals(response.code(), HttpURLConnection.HTTP_INTERNAL_ERROR)
        assertEquals(response.message(), "Server Error")
    }

    @Test
    fun `get 404 error coin list from server`() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
        mockServer.enqueue(mockResponse)
        val response = serviceEndPoints.getCoins()
        mockServer.takeRequest()
        assertEquals(false, response.isSuccessful)
        assertNotNull(response.errorBody())
        assertNull(response.body())
        assertEquals(response.code(), HttpURLConnection.HTTP_NOT_FOUND)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }
}