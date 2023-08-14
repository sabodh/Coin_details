package com.online.coinpaprika.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.presentation.theme.CardColor
import com.online.coinpaprika.presentation.theme.CardColorText

/**
 * Used for Coin list
 */
@Composable
fun CoinListItem(coin: CoinDetails, selectedCoin: (CoinDetails) -> Unit) {
    Card(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .clickable { selectedCoin(coin) },
        elevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardColor),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image logo
            LogoImage(coin.logo, modifier = Modifier
                .padding(20.dp)
                .padding(2.dp))
            // Coin details
            CardText(coin = coin)

        }
    }

}


@Composable
fun CardText(coin: CoinDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CardColorText)
            .padding(all = 15.dp),
        verticalArrangement = Arrangement.Center

    ) {
        Details(coin.name, style = MaterialTheme.typography.h6)
        Details(coin.symbol, style = MaterialTheme.typography.subtitle1)
        Details(coin.type, style = MaterialTheme.typography.subtitle1)
    }
}

