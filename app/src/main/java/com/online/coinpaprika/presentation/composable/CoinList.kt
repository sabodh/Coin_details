package com.online.coinpaprika.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.data.model.CoinList

@Composable
fun CoinList(coin: CoinList, selectedCoin :(CoinDetails) -> Unit){
    LazyColumn(
        contentPadding = PaddingValues(all = 5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxWidth()

    ) {
        items(coin) { item ->
                CoinListItem(coin = item, selectedCoin)
        }
    }
}
