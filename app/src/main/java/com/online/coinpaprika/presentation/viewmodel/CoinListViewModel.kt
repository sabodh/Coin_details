package com.online.coinpaprika.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.coinpaprika.data.api.ServiceResponse
import com.online.coinpaprika.data.model.CoinList
import com.online.coinpaprika.domain.usecases.CoinListUseCase
import com.online.coinpaprika.utils.Constants
import com.online.coinpaprika.utils.ErrorCode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val coinListUseCase: CoinListUseCase
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

    private val _coinList = MutableStateFlow(CoinList())

    /**
     * Filter the coin based on the tag search keyword
     */
    @OptIn(FlowPreview::class)
    val searchResult = searchText
        .debounce(1000L)
        .onEach { _isSearching.update { true }}
        .combine(_coinList) { text, coins ->
            if (text.isBlank()) {
                coins
            } else {
                coins.filter {
                    it.tags.any { tag ->
                        tag.name.equals(text, ignoreCase = true)
                    }
                }
            }
        }
        .onEach { _isSearching.update { false }}
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _coinList.value
        )

    /**
     * Search the keyword
     */
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    /**
     * For set the latest coin details
     */
    fun provideCoinList(coinList: CoinList){
        _coinList.value = coinList
    }

    /**
     * Get the coin details from server
     */
    fun getCoinList() {
        viewModelScope.launch {
            coinListUseCase().catch { e ->
                _coinsState.value =
                    ServiceResponse.Error(
                        ErrorCode.UNKNOWN_ERROR.statusCode,
                        e.message ?: Constants.COMMON_ERROR_MESSAGE
                    )
            }.collect {
                _coinsState.value = it
            }
        }
    }
}