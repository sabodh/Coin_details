package com.online.coinpaprika.presentation.composable

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.presentation.loadProgressbar
import com.online.coinpaprika.presentation.theme.CoinpaprikaTheme
import com.online.coinpaprika.presentation.viewmodel.CoinDetailsViewModel
import com.online.coinpaprika.presentation.viewmodel.factory.CoinDetailsViewModelFactory


@Composable
fun CoinDetails(
    navController: NavController,
    coinDetailsViewModelFactory: CoinDetailsViewModelFactory,
    coinId: String
) {
    val coinViewModel: CoinDetailsViewModel = viewModel(
        factory = coinDetailsViewModelFactory
    )
    DetailedView(navController = navController, coinViewModel, coinId = coinId)

}

@Composable
fun DetailedView(
    navController: NavController,
    coinViewModel: CoinDetailsViewModel,
    coinId: String

) {

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = Unit) {
        getCoinDetails(coinViewModel, coinId)
    }

    val response = coinViewModel.coinDetailsState.collectAsState()
    when (response.value) {
        is ServiceResponse.Error -> {
            Log.e("CoinDetails", "error")
            ShowSnackBar(snackbarHostState, (response.value as ServiceResponse.Error).message)
        }
        ServiceResponse.Loading -> {
            Log.e("CoinDetails", "Loading")
            loadProgressbar()
        }
        is ServiceResponse.Success -> {
            Log.e("CoinDetails", "Success")
            val context = LocalContext.current
            ShowCoinDetails(coinDetails = (response.value as
                    ServiceResponse.Success<CoinDetails>).data)
        }
        else -> {
            Log.e("CoinDetails", "else")
            ShowSnackBar(snackbarHostState, "Unknown Error")
        }
    }
}

fun getCoinDetails(coinViewModel: CoinDetailsViewModel, coinId: String) {
    coinViewModel.getCoinDetails(coinId)
}

@Composable
fun ShowCoinDetails(coinDetails: CoinDetails) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoImage(coinDetails.logo, modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .padding(top = 60.dp))
            Details(coinDetails.symbol, textAlign = TextAlign.Center)
            Divider(startIndent = 2.dp, thickness = 1.dp, color = Color.LightGray)
            Details(coinDetails.name)
            Divider(startIndent = 2.dp, thickness = 1.dp, color = Color.LightGray)
            Details(coinDetails.type)
            Divider(startIndent = 2.dp, thickness = 1.dp, color = Color.LightGray)
            Details(coinDetails.description, style = MaterialTheme.typography.body2)
            Divider(startIndent = 2.dp, thickness = 1.dp, color = Color.LightGray)
        }
    }
}

@Composable
fun ShowSnackBar(snackbarHostState: SnackbarHostState, message: String) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           LaunchedEffect(key1 = snackbarHostState) {
               snackbarHostState.showSnackbar(message)
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun LogoImage(imageUrl: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        GlideImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
fun Details(
    content: String, style: TextStyle = MaterialTheme.typography.subtitle1,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = content,
        textAlign = textAlign,
        fontWeight = FontWeight.Normal,
        style = style,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoinpaprikaTheme {
//        ShowCoinDetails(coinDetails = CoinDetails())
    }
}
