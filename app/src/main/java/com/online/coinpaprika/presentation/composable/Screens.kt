package com.online.coinpaprika.presentation.composable

import com.online.coinpaprika.utils.Constants.COIN_ID
import com.online.coinpaprika.utils.Constants.COMPOSABLE_DETAILS_SCREEN
import com.online.coinpaprika.utils.Constants.COMPOSABLE_HOME_SCREEN

/**
 * Used to handle navGraph for screen navigation
 */
sealed class Screens(val route: String){
    object Home: Screens(route = COMPOSABLE_HOME_SCREEN)
    object Details: Screens(route = "$COMPOSABLE_DETAILS_SCREEN/{$COIN_ID}"){
        fun passId(id: String): String{
            return this.route.replace(oldValue = "{$COIN_ID}", newValue = id )
        }
    }
}
