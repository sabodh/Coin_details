package com.online.coinpaprika.presentation.viewmodel.factory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.online.coinpaprika.domain.usecases.CoinDetailsUseCase
import com.online.coinpaprika.presentation.viewmodel.CoinDetailsViewModel

class CoinDetailsViewModelFactory(private val useCase: CoinDetailsUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinDetailsViewModel::class.java)) {
            return CoinDetailsViewModel(useCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}