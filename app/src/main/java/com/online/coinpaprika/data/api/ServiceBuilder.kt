package com.online.coinpaprika.data.api

import com.online.coinpaprika.utils.Constants.BASE_URL
import com.online.coinpaprika.utils.Constants.HTTP_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceBuilder {

    private val client = OkHttpClient.Builder()
        .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun <T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

}