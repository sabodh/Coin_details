package com.online.coinpaprika.presentation.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import com.online.coinpaprika.utils.ErrorCode
import com.online.coinpaprika.utils.TestData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class CoinListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var conListUseCase: CoinListUseCase
    private lateinit var viewModel: CoinListViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `get success coin list from view-model`() = runTest {
        viewModel = CoinListViewModel(conListUseCase)
        val coinList = TestData.coinList
        val coinListResponse = ServiceResponse.Success(coinList)
        Mockito.`when`(conListUseCase())
            .thenReturn(flowOf(coinListResponse))
        viewModel.getCoinList()
        Assert.assertNotNull(viewModel.coinsState.value)
        Assert.assertEquals(coinListResponse, viewModel.coinsState.value)
    }
    @Test
    fun `get error from view-model`() = runTest {
        val unknownError = "Unknown error"
        val coinListResponse = ServiceResponse.Error(ErrorCode.UNKNOWN_ERROR.statusCode, unknownError)
        viewModel = CoinListViewModel(conListUseCase)
        Mockito.`when`(conListUseCase())
            .thenReturn(flowOf(coinListResponse))
        viewModel.getCoinList()
        Assert.assertNotNull(viewModel.coinsState.value)
        Assert.assertEquals(coinListResponse, viewModel.coinsState.value)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}