package com.uyjang.korbittest.viewModel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uyjang.korbittest.base.UiState
import com.uyjang.korbittest.data.internal.FavoriteDataSource
import com.uyjang.korbittest.data.remote.model.TickerDetail
import com.uyjang.korbittest.data.remote.repository.ApiResult
import com.uyjang.korbittest.data.remote.repository.ApiServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainViewModel(application: Application) : AndroidViewModel(application) {
    init {
        // 코루틴을 사용하여 getPreference의 Flow 값을 가져온 후 StateFlow에 설정
        viewModelScope.launch {
            repository = ApiServiceRepository()
            favoriteDataSource = FavoriteDataSource(getApplication())
            initMarketData()
            // 리스트 조회 결과는 거래대금이 높은 순서대로 정렬하여 출력
            sortList(sortButtonNum)
        }
    }

    // UIState(현재 ui 상태 확인용)
    var viewState by mutableStateOf<UiState<Unit>>(UiState.None)

    // Api 연결
    lateinit var repository: ApiServiceRepository
    var marketDataList: List<MarketData>? = null

    // 마켓, 즐겨찾기 리스트
    var marketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())
    var favoriteMarketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())

    // 검색 기능
    var searchText by mutableStateOf("")

    // 정렬 기능
    var sortButtonNum by mutableIntStateOf(41)

    // 검색, 정렬을 위해 복구용 백업 리스트
    private var tempMarketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())
    private var tempFavoriteMarketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())

    // 즐겨찾기 기능
    private lateinit var favoriteDataSource: FavoriteDataSource
    private var favorites by mutableStateOf(emptyList<String>())

    // 즐겨찾기 설정
    fun setFavorites(favorite: String) {
        if (favorites.contains(favorite)) {
            favorites = favorites.filter { it != favorite }
        } else {
            favorites += favorite
        }
        viewModelScope.launch {
            favoriteDataSource.setPreference(favorites)
            initMarketData()
            sortList(sortButtonNum)
        }
    }

    private fun getFavorites(): Flow<List<String>> {
        return favoriteDataSource.getPreference()
    }

    // api 호출
    private suspend fun getTickerDetails() {
        Toast.makeText(getApplication(), "모든 시장 현황 api 요청합니다.", Toast.LENGTH_SHORT).show()
        var tickerDetails by mutableStateOf<Map<String, TickerDetail>>(emptyMap())

        repository.fetchTickerDetails().collectLatest {result ->
            tickerDetails = when (result) {
                is ApiResult.Loading -> {
                    viewState = UiState.Loading
                    tickerDetails
                }
                is ApiResult.Error -> {
                    viewState = UiState.Error("api 요청에 실패하였습니다.")
                    emptyMap()
                }
                is ApiResult.Success -> {
                    viewState = UiState.None
                    result.data
                }
            }
        }

        marketDataList = tickerDetails.map { (currencyPair, tickerDetail) ->
            MarketData(
                currencyPair = currencyPair,
                timestamp = tickerDetail.timestamp,
                last = tickerDetail.last,
                open = tickerDetail.open,
                bid = tickerDetail.bid,
                ask = tickerDetail.ask,
                low = tickerDetail.low,
                high = tickerDetail.high,
                volume = tickerDetail.volume,
                change = tickerDetail.change,
                changePercent = tickerDetail.changePercent
            )
        }
    }

    suspend fun initMarketData() {
        favorites = getFavorites().first()

        // 처음에만 수행
        if (marketDataList == null) {
            // api 조회
            getTickerDetails()
        }

        // view용 마켓데이터 전처리
        marketDataPreprocessedDataList = marketDataList!!.map { marketData ->
            val currencyPair = marketData.currencyPair.uppercase().replace("_", "/")
            val lastPrice = formatPrice(marketData.last.toDouble())
            val priceChangeRateDouble = marketData.changePercent.toDouble()
            val priceChangeRate =
                if (priceChangeRateDouble > 0) "+${marketData.changePercent}%"
                else "${String.format("%.2f", priceChangeRateDouble)}%"
            val priceChangePrice = formatPrice(marketData.change.toDouble()).let {
                if (priceChangeRateDouble > 0) "+$it" else it
            }
            val tradeVolume = marketData.volume

            MarketDataPreprocessedData(
                ShowMarketData(
                    currencyPair = currencyPair,
                    lastPrice = lastPrice,
                    priceChangeRate = priceChangeRate,
                    priceChangePrice = priceChangePrice,
                    tradeVolume = String.format("%,d", tradeVolume.toDouble().toInt()),
                    favorite = currencyPair in favorites
                ),
                originalMarketData = marketData
            )
        }

        // 즐겨찾기 리스트
        favoriteMarketDataPreprocessedDataList = marketDataPreprocessedDataList.filter { data ->
            data.showMarketData.currencyPair in favorites
        }

        // 복구용 리스트 저장
        tempMarketDataPreprocessedDataList = marketDataPreprocessedDataList
        tempFavoriteMarketDataPreprocessedDataList = favoriteMarketDataPreprocessedDataList
    }

    private fun formatPrice(value: Double): String {
        return when {
            abs(value) >= 100 -> {
                String.format("%,d", value.toInt())
            }

            else -> {
                String.format("%,.2f", value)
            }
        }
    }

    fun searchMarket(searchText: String) {
        this.searchText = searchText
        marketDataPreprocessedDataList = tempMarketDataPreprocessedDataList.filter { data ->
            data.showMarketData.currencyPair.contains(searchText, ignoreCase = true)
        }
        favoriteMarketDataPreprocessedDataList =
            tempFavoriteMarketDataPreprocessedDataList.filter { data ->
                data.showMarketData.currencyPair.contains(searchText, ignoreCase = true)
            }
    }

    fun sortList(sortButtonNum: Int) {
        when (sortButtonNum) {
            // 정렬 버튼 - 가상자산명 내림차순
            11 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.currencyPair }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.currencyPair }
            }
            // 정렬 버튼 - 가상자산명 오름차순
            12 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.currencyPair }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.currencyPair }
            }
            // 정렬 버튼 - 현재가 내림차순
            21 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.last.toDouble() }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.last.toDouble() }
            }
            // 정렬 버튼 - 현재가 오름차순
            22 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.last.toDouble() }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.last.toDouble() }
            }
            // 정렬 버튼 - 24시간 내림차순
            31 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.changePercent.toDouble() }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.changePercent.toDouble() }
            }
            // 정렬 버튼 - 24시간 오름차순
            32 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.changePercent.toDouble() }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.changePercent.toDouble() }
            }
            // 정렬 버튼 - 거래대금 내림차순
            41 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.volume.toDouble() }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.volume.toDouble() }
            }
            // 정렬 버튼 - 거래대금 오름차순
            42 -> {
                tempMarketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.volume.toDouble() }
                tempFavoriteMarketDataPreprocessedDataList =
                    tempFavoriteMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.volume.toDouble() }
            }

            else -> {}
        }
        marketDataPreprocessedDataList = tempMarketDataPreprocessedDataList
        favoriteMarketDataPreprocessedDataList = tempFavoriteMarketDataPreprocessedDataList

        searchMarket(searchText)
    }
}

// view용 마켓데이터 전처리 데이터
data class MarketDataPreprocessedData(
    val showMarketData: ShowMarketData,
    val originalMarketData: MarketData
)

data class ShowMarketData(
    val currencyPair: String, // 가상자산명
    val lastPrice: String, // 현재가
    val priceChangeRate: String, // 변동률
    val priceChangePrice: String, // 변동가격
    val tradeVolume: String, // 거래대금
    val favorite: Boolean // 즐겨찾기
)

data class MarketData(
    val currencyPair: String,
    val timestamp: Long,
    val last: String,
    val open: String,
    val bid: String,
    val ask: String,
    val low: String,
    val high: String,
    val volume: String,
    val change: String,
    val changePercent: String
)