package com.online.coinpaprika.data.api

import com.online.coinpaprika.data.model.Coin
import com.online.coinpaprika.data.model.CoinDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceEndPoints {
    @GET("v1/coins")
    suspend fun getCoins() : Response<Coin>

    @GET("v1/coins/{coinId}")
    suspend fun getCoinDetails(@Path("coinId") coinId: String): Response<CoinDetails>

}