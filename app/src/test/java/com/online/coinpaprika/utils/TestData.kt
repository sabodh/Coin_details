package com.online.coinpaprika.utils

import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.data.model.CoinList

object TestData {

    private val coinDetails_1 =
        CoinDetails(
            description = "",
            development_status = "",
            first_data_at = "",
            hardware_wallet = false,
            hash_algorithm = "",
            id = "eth-ethereum",
            is_active = false,
            is_new = false,
            last_data_at = "",
            logo = "",
            message = "",
            name = "Ethereum",
            open_source = false,
            org_structure = "",
            proof_type = "",
            rank = 0,
            started_at = "",
            symbol = "",
            tags = emptyList(),
            type = ""
        )
    private val coinDetails_2 = CoinDetails(
        description = "",
        development_status = "",
        first_data_at = "",
        hardware_wallet = false,
        hash_algorithm = "",
        id = "usdt-tether",
        is_active = false,
        is_new = false,
        last_data_at = "",
        logo = "",
        message = "",
        name = "Tether",
        open_source = false,
        org_structure = "",
        proof_type = "",
        rank = 0,
        started_at = "",
        symbol = "",
        tags = emptyList(),
        type = ""
    )
    private val coinDetails_3 = CoinDetails(
        description = "",
        development_status = "",
        first_data_at = "",
        hardware_wallet = false,
        hash_algorithm = "",
        id = "bnb-binance-coin",
        is_active = false,
        is_new = false,
        last_data_at = "",
               logo = "",
        message = "",
        name = "Binance Coin",
        open_source = false,
        org_structure = "",
        proof_type = "",
        rank = 0,
        started_at = "",
        symbol = "",
        tags = emptyList(),

        type = ""
    )
    private val coinDetailsList = listOf(
        coinDetails_1,
        coinDetails_2,
        coinDetails_3,
    )
    val coinList = CoinList().apply {
        addAll(coinDetailsList)
    }

}