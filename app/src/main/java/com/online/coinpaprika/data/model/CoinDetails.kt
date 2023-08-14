package com.online.coinpaprika.data.model

data class CoinDetails(
    val description: String,
    val development_status: String,
    val first_data_at: String,
    val hardware_wallet: Boolean,
    val hash_algorithm: String,
    val id: String,
    val is_active: Boolean,
    val is_new: Boolean,
    val last_data_at: String,
    val logo: String,
    val message: String,
    val name: String,
    val open_source: Boolean,
    val org_structure: String,
    val proof_type: String,
    val rank: Int,
    val started_at: String,
    val symbol: String,
    val tags: List<Tag>,
    val type: String
)
{
    companion object {
        val default: CoinDetails
            get() = CoinDetails(
                description = "",
                development_status = "",
                first_data_at = "",
                hardware_wallet = false,
                hash_algorithm = "",
                id = "bit-coin",
                is_active = false,
                is_new = false,
                last_data_at = "",
                logo = "",
                message = "",
                name = "BitCoin",
                open_source = false,
                org_structure = "",
                proof_type = "",
                rank = 0,
                started_at = "",
                symbol = "",
                tags = emptyList(),
                type = ""
            )
    }
}