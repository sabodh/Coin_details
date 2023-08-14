package com.online.coinpaprika.presentation.composable


import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.online.coinpaprika.presentation.viewmodel.factory.CoinDetailsViewModelFactory
import com.online.coinpaprika.presentation.viewmodel.factory.CoinListViewModelFactory
import com.online.coinpaprika.utils.Constants.COIN_ID

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    coinListViewModelFactory: CoinListViewModelFactory,
    coinDetailsViewModelFactory: CoinDetailsViewModelFactory
) {
    NavHost(
        navController = navController, startDestination = Screens.Home.route
    ) {
        // Composable for home screen
        composable(
            route = Screens.Home.route
        ) {
            HomeScreen(navController, coinListViewModelFactory)
        }
        // Composable for details screen, having coinId as arguments
        composable(
            route = Screens.Details.route,
            arguments = listOf(navArgument(COIN_ID) {
                type = NavType.StringType
            })
        ) {
            val coinId = it.arguments?.getString(COIN_ID).toString()
            CoinDetails(navController, coinDetailsViewModelFactory, coinId)
        }
    }

}
