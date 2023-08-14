package com.online.coinpaprika.presentation.viewmodel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.utils.ErrorCode
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
class CoinDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var coinDetailsUseCase: CoinDetailsUseCase
    private lateinit var viewModel: CoinDetailsViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `get success coin details from use-case`() = runTest {
        val coinId = "bit-coin"
        viewModel = CoinDetailsViewModel(coinDetailsUseCase)
        val coinListResponse = ServiceResponse.Success(CoinDetails.default)
        Mockito.`when`(coinDetailsUseCase(coinId))
            .thenReturn(flowOf(coinListResponse))
        viewModel.getCoinDetails(coinId)
        Assert.assertNotNull(viewModel.coinDetailsState.value)
        Assert.assertEquals(coinListResponse, viewModel.coinDetailsState.value)
    }
    @Test
    fun `get error from use-case`() = runTest {
        val coinId = "bit-coin"
        val errorMessage = "Unknown error"
        viewModel = CoinDetailsViewModel(coinDetailsUseCase)
        val coinListResponse = ServiceResponse.Error(ErrorCode.UNKNOWN_ERROR.statusCode, errorMessage)
        Mockito.`when`(coinDetailsUseCase(coinId))
            .thenReturn(flowOf(coinListResponse))
        viewModel.getCoinDetails(coinId)
        Assert.assertNotNull(viewModel.coinDetailsState.value)
        Assert.assertEquals(coinListResponse, viewModel.coinDetailsState.value)
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}