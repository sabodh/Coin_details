package com.online.coinpaprika.presentation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.online.coinpaprika.data.api.ServiceBuilder
import com.online.coinpaprika.data.api.ServiceEndPoints
import com.online.coinpaprika.data.repository.CoinRepositoryImpl
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import com.online.coinpaprika.presentation.composable.SetupNavGraph
import com.online.coinpaprika.presentation.theme.CoinpaprikaTheme
import com.online.coinpaprika.presentation.viewmodel.factory.CoinDetailsViewModelFactory
import com.online.coinpaprika.presentation.viewmodel.factory.CoinListViewModelFactory


class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val serviceBuilder = ServiceBuilder.buildService(ServiceEndPoints::class.java)
        val repository = CoinRepositoryImpl(serviceBuilder)
        val coinListUseCase = CoinListUseCase(repository)
        val coinDetailsUseCase = CoinDetailsUseCase(repository)
        val viewModelFactory = CoinListViewModelFactory(coinListUseCase)
        val viewModelDetailsViewModelFactory = CoinDetailsViewModelFactory(coinDetailsUseCase)
        setContent {
            CoinpaprikaTheme {               
                navController = rememberNavController()
                SetupNavGraph(navController = navController, viewModelFactory, viewModelDetailsViewModelFactory)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoinpaprikaTheme {

    }
}

@Composable
fun LoadProgressbar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}