package com.online.coinpaprika.presentation.composable

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.online.coinpaprika.R
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.presentation.LoadProgressbar
import com.online.coinpaprika.presentation.theme.BackgroundBlack
import com.online.coinpaprika.presentation.viewmodel.CoinListViewModel
import com.online.coinpaprika.presentation.viewmodel.factory.CoinListViewModelFactory
import com.online.coinpaprika.utils.ErrorCode
import kotlinx.coroutines.launch



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
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    // invoking view-model initially
    LaunchedEffect(key1 = true) {
        getCoinList(coinViewModel)
    }

    // pull to refresh
    fun refresh() = refreshScope.launch {
        refreshing = true
        getCoinList(coinViewModel)
    }

    val context = LocalContext.current

    val state = rememberPullRefreshState(refreshing, ::refresh)
    val response = coinViewModel.coinsState.collectAsState()
    when (response.value) {
        is ServiceResponse.Error -> {
            refreshing = false
            HomeCoinList(
                navController = navController,
                state = state,
                coinViewModel = coinViewModel
            )
            ShowSnackBar(
                SnackbarHostState(),
                getErrorMessage(
                    context,
                    (response.value as ServiceResponse.Error).errorCode,
                    (response.value as ServiceResponse.Error).message
                )
            )
        }
        ServiceResponse.Loading -> {
            LoadProgressbar()
        }
        is ServiceResponse.Success -> {
            refreshing = false
            // getting the data from response
            val list = (response.value as ServiceResponse.Success<CoinList>).data
            // set the data for list and used for search as well
            LaunchedEffect(response.value) {
                coinViewModel.provideCoinList(list)
            }
            HomeCoinList(
                navController = navController,
                state = state,
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
    coinViewModel: CoinListViewModel
) {
    val searchText by coinViewModel.searchText.collectAsState()
    val coinDetails by coinViewModel.searchResult.collectAsState()
    val isSearching by coinViewModel.isSearching.collectAsState()
    val convertedCoinList = CoinList().apply {
        addAll(coinDetails)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlack),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = searchText, onValueChange = coinViewModel::onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 6.dp, end = 6.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = BackgroundBlack,
                cursorColor = Color.Black
            ),

            placeholder = { Text(text = "Search") })
        if (isSearching) {
            LoadProgressbar()
        } else {
            Box(
                modifier = Modifier
                    .pullRefresh(state)
                    .background(BackgroundBlack),
                contentAlignment = Alignment.BottomCenter,

                ) {

                // Coin List items
                CoinList(coin = convertedCoinList) {
                    navController.navigate(route = Screens.Details.passId(it.id))
                }
                // pull to refresh screen
                PullRefreshIndicator(
                    refreshing, state,
                    Modifier
                        .align(Alignment.TopCenter),
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

fun getErrorMessage(context: Context, error: Int, message: String = ""): String {
    return when (error) {
        ErrorCode.NETWORK_ERROR.statusCode -> {
            context.getString(R.string.error_network)
        }
        ErrorCode.UNKNOWN_ERROR.statusCode -> {
            context.getString(R.string.error_unknown)
        }
        ErrorCode.SERVER_ERROR.statusCode -> {
            context.getString(R.string.error_server)
        }
        else -> {
            message
        }
    }
}