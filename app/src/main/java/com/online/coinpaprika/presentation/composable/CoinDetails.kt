package com.online.coinpaprika.presentation.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.presentation.LoadProgressbar
import com.online.coinpaprika.presentation.theme.BackgroundGray
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
    LaunchedEffect(key1 = Unit) {
        getCoinDetails(coinViewModel, coinId)
    }
    val context = LocalContext.current

    val response = coinViewModel.coinDetailsState.collectAsState()
    when (response.value) {
        is ServiceResponse.Error -> {

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
            ShowCoinDetails(
                navController = navController,
                coinDetails = (response.value as
                        ServiceResponse.Success<CoinDetails>).data
            )
        }
    }
}

/**
 * Show coin details
 */
fun getCoinDetails(coinViewModel: CoinDetailsViewModel, coinId: String) {
    coinViewModel.getCoinDetails(coinId)
}

@Composable
fun ShowCoinDetails(navController: NavController, coinDetails: CoinDetails) {
    Box(
        Modifier
            .fillMaxSize()
            .background(BackgroundGray),
        contentAlignment = Alignment.TopStart,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(Icons.Filled.ArrowBack, "back",
                modifier = Modifier
                    .padding(start = 2.dp)
                    .clickable {
                        navController.navigate(Screens.Home.route) {
                            popUpTo(Screens.Home.route) {
                                inclusive = true
                            }
                        }
                    })
            LogoImage(
                coinDetails.logo, modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(top = 60.dp)
            )


            DetailsSession(
                coinDetails.symbol,
                style = customStyleBold,
                textAlign = TextAlign.Center
            )
            DetailsSession(coinDetails.name, style = customStyle)
            DetailsSession(coinDetails.type, style = customStyle)
            DetailsSession(coinDetails.description, style = MaterialTheme.typography.body1)

        }
    }
}

val customStyle = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
)
val customStyleBold = TextStyle(
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    letterSpacing = 0.15.sp
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoinpaprikaTheme {
//        ShowCoinDetails(coinDetails = CoinDetails.default)
    }
}
