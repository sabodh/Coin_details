package com.online.coinpaprika.presentation.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.presentation.loadProgressbar
import com.online.coinpaprika.presentation.viewmodel.CoinListViewModel
import com.online.coinpaprika.presentation.viewmodel.factory.CoinListViewModelFactory
import kotlinx.coroutines.launch

const val TAG = "Home Screen"

@Composable
fun HomeScreen(
    navController: NavController,
    coinListViewModelFactory: CoinListViewModelFactory
) {
    val coinViewModel: CoinListViewModel = viewModel(
        factory = coinListViewModelFactory
    )
    ShowCoinList(navController, coinViewModel)
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowCoinList(
    navController: NavController, coinViewModel: CoinListViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    // invoking view-model initially
    LaunchedEffect(key1 = true) {
        getCoinList(coinViewModel)
    }

    fun refresh() = refreshScope.launch {
        refreshing = true
        getCoinList(coinViewModel)
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)
    val response = coinViewModel.coinsState.collectAsState()
    when (response.value) {
        is ServiceResponse.Error -> {
            Log.e(TAG, "error${(response.value as ServiceResponse.Error).message}")
            refreshing = false
            HomeCoinList(
                navController = navController,
                state = state,
                coinList = CoinList(),
                coinViewModel = coinViewModel
            )
            ShowSnackBar(snackbarHostState, "Unknown Error")
        }
        ServiceResponse.Loading -> {
            Log.e(TAG, "Loading")
            loadProgressbar()
        }
        is ServiceResponse.Success -> {
            Log.e(TAG, "Success")
            refreshing = false
            val list = (response.value as ServiceResponse.Success<CoinList>).data
            coinViewModel.setCoinDetails(list)
            HomeCoinList(
                navController = navController,
                state = state,
                coinList = list,
                coinViewModel = coinViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeCoinList(
    navController: NavController,
    state: PullRefreshState,
    refreshing: Boolean = false,
    coinList: CoinList, coinViewModel: CoinListViewModel
) {
    val searchText by coinViewModel.searchText.collectAsState()
    val coinDetails by coinViewModel.searchResult.collectAsState()
    val isSearching by coinViewModel.isSearching.collectAsState()
    Log.e(TAG, "coinDetails: "+coinDetails.size)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = searchText, onValueChange = coinViewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp),
            placeholder = { Text(text = "Search") })

        if (isSearching) {
            loadProgressbar()
        } else {
            Box(
                modifier = Modifier
                    .pullRefresh(state)
                    .background(Color.LightGray),
                contentAlignment = Alignment.BottomCenter,

                ) {

                // Coin List items
                CoinList(coin = coinList) {
                    navController.navigate(route = Screens.Details.passId(it.id))
                }
                // pull to refresh screen
                PullRefreshIndicator(
                    refreshing, state,
                    Modifier
                        .align(Alignment.TopCenter)     ,
                    backgroundColor = Color.Transparent
                )
            }
        }


    }


}

/**
 * Invoking the view-model
 */
fun getCoinList(coinViewModel: CoinListViewModel) {
    coinViewModel.getCoinList()
}