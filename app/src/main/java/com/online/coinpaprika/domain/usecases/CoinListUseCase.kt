package com.online.coinpaprika.domain.usecases

import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.domain.repository.CoinRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinListUseCase (private val coinRepository: CoinRepository) {
    suspend operator fun invoke() : Flow<ServiceResponse<CoinList>> {
        val response = coinRepository.getCoins().map { coinListResponse ->
            if (coinListResponse is ServiceResponse.Success) {
                val coinList = coinListResponse.data
                // Sort the objects based on it's name
                val mergedCoinList = CoinList().apply {
                    addAll(coinList.sortedBy { it.name })
                }
                ServiceResponse.Success(mergedCoinList)
            } else {
                coinListResponse
            }
        }
        return response
    }

}