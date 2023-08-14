package com.online.coinpaprika.domain.usecases
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.domain.repository.CoinRepository
import com.online.coinpaprika.utils.ErrorCode
import com.online.coinpaprika.utils.TestData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.flow.single
import org.junit.Assert

@RunWith(MockitoJUnitRunner::class)
class CoinListUseCaseTest {
    @Mock
    private lateinit var mockCoinRepository: CoinRepository

    private lateinit var coinListUseCase: CoinListUseCase

    @Before
    fun setup() {
        coinListUseCase = CoinListUseCase(mockCoinRepository)
    }
    @Test
    fun `successfully get sorted coin list based on coin name `() = runBlocking {

        val convertedCoinList = TestData.coinList
        val coinListResponse = ServiceResponse.Success(convertedCoinList)
        Mockito.`when`(mockCoinRepository.getCoins()).
        thenReturn(flowOf(coinListResponse))
        val result = coinListUseCase.invoke().single()
        Assert.assertNotNull(result)
        val mergedCoinList = (result as ServiceResponse.Success).data
        Assert.assertEquals(3, mergedCoinList.size)
        Assert.assertEquals("Binance Coin", mergedCoinList[0].name)
        Assert.assertEquals("Ethereum", mergedCoinList[1].name)
        Assert.assertEquals("Tether", mergedCoinList[2].name)
    }

    @Test
    fun `invoke error from server`() = runBlocking {
        val errorMessage = "An error occurred"
        val errorResponse = ServiceResponse.Error(ErrorCode.UNKNOWN_ERROR.statusCode, errorMessage)
        Mockito.`when`(mockCoinRepository.getCoins()).
        thenReturn(flowOf(errorResponse))
        val result = coinListUseCase.invoke().single()
        Assert.assertNotNull(result)
        Assert.assertEquals(errorMessage,
            (result as ServiceResponse.Error).message)
        Assert.assertEquals(ErrorCode.UNKNOWN_ERROR.statusCode,
            result.errorCode)
    }


}