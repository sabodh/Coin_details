package com.online.coinpaprika.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinDetails
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinListViewModel(
    val coinListUseCase: CoinListUseCase
) : ViewModel() {

    private val _coinsState = MutableStateFlow<ServiceResponse<CoinList>>(
        ServiceResponse.Loading
    )
    val coinsState: StateFlow<ServiceResponse<CoinList>>
        get() = _coinsState

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private var _coinList = CoinList()
    fun setCoinDetails(list: CoinList) {
        _coinList = list
    }


//    val filteredCoins = filterCoinsByTag(_coinList, "Mining")

    val searchCoins = MutableStateFlow(_coinList)


    fun filterCoinsByTag(coinList: List<CoinDetails>, tagName: String): List<CoinDetails> {
        return coinList.filter { coin ->
            coin.tags.any { tag -> tag.name == tagName }
        }
    }


    val searchResult = searchText
        .combine(searchCoins) { text, coins ->
            if (text.isBlank()) {
                coins
            } else {
                coins.filter {
                    it.tags.any { tag -> tag.name == text }
                }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            searchCoins.value
        )


//    val searchResult = searchText
//        .debounce(1000L)
//        .onEach { _isSearching.update { true } }
//        .combine(searchCoins) { text, coins ->
//            if (text.isBlank()) {
//                coins
//            } else {
////                delay(2000L)
//                coins.filter {
//                    it.tags.any { tag -> tag.name == text }
//                }
//            }
//        }
//        .onEach { _isSearching.update { false } }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000),
//            searchCoins.value
//        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getCoinList() {
        viewModelScope.launch {
            coinListUseCase().catch { e ->
                _coinsState.value = ServiceResponse.Error(e.message ?: "Unknown Error")
            }.collect {
                _coinsState.value = it
            }
        }
    }
}