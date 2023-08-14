package com.online.coinpaprika.domain.repository

import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.data.model.CoinList
import kotlinx.coroutines.flow.Flow

interface CoinRepository {

    suspend fun getCoins(): Flow<ServiceResponse<CoinList>>

    suspend fun getCoinDetails(coinId: String): Flow<ServiceResponse<CoinDetails>>

}