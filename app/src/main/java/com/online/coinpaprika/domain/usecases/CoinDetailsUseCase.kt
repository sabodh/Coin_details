package com.online.coinpaprika.domain.usecases

import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow

class CoinDetailsUseCase(private val coinRepository: CoinRepository) {

    suspend operator fun invoke(coinId: String): Flow<ServiceResponse<CoinDetails>> {
        return coinRepository.getCoinDetails(coinId)
    }
}