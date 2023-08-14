package com.online.coinpaprika.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.utils.Constants
import com.online.coinpaprika.utils.ErrorCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CoinDetailsViewModel(
    private val coinDetailsUseCase: CoinDetailsUseCase
) : ViewModel() {
    private val _coinDetailsState =
        MutableStateFlow<ServiceResponse<CoinDetails>>(ServiceResponse.Loading)
    val coinDetailsState = _coinDetailsState


    /**
     * Get the coin details from server based on coinId.
     */
    fun getCoinDetails(coinId: String) {
        viewModelScope.launch {
            coinDetailsUseCase(coinId).catch { e ->
                _coinDetailsState.value = ServiceResponse.Error(
                    ErrorCode.UNKNOWN_ERROR.statusCode,
                    e.message ?: Constants.COMMON_ERROR_MESSAGE
                )
            }.collect {
                _coinDetailsState.value = it
            }
        }
    }
}