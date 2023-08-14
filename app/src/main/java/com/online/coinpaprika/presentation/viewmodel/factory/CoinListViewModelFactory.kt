package com.online.coinpaprika.presentation.viewmodel.factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import com.online.coinpaprika.presentation.viewmodel.CoinListViewModel

class CoinListViewModelFactory(private val useCase: CoinListUseCase) : Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinListViewModel::class.java)) {
            return CoinListViewModel(useCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}