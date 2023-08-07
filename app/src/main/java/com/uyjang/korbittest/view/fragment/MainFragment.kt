package com.uyjang.korbittest.view.fragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.uyjang.korbittest.base.BaseFragment
import com.uyjang.korbittest.view.compose.MarketTabScreen
import com.uyjang.korbittest.view.compose.SearchTopBar
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme
import com.uyjang.korbittest.viewModel.MainViewModel
import com.uyjang.korbittest.viewModel.MarketData
import com.uyjang.korbittest.viewModel.MarketDataPreprocessedData
import com.uyjang.korbittest.viewModel.ShowMarketData

class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by viewModels()

    init {
        baseCompose.content = {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SearchTopBar()
                    MarketTabScreen(viewModel.marketDataPreprocessedDataList)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMarketList() {
    val marketDataPreprocessedDataList: List<MarketDataPreprocessedData> = listOf(
        MarketDataPreprocessedData(
            ShowMarketData(
                currencyPair = "hnt/krw",
                lastPrice = "2,523",
                priceChangeRate = "+0.24%",
                priceChangePrice = "+6.00",
                tradeVolume = "45"
            ),
            MarketData(
                currencyPair = "hnt_krw",
                timestamp = 1691301159586,
                last = "2900",
                open = "2900",
                bid = "2522",
                ask = "2900",
                low = "2488",
                high = "2900",
                volume = "45.23423",
                change = "1",
                changePercent = "1"
            )
        ),
        MarketDataPreprocessedData(
            ShowMarketData(
                currencyPair = "bch/krw",
                lastPrice = "300,500",
                priceChangeRate = "-0.87%", // (300500 - 297900) / 297900 * 100
                priceChangePrice = "-2,600", // 297900 - 300500
                tradeVolume = "1,351"
            ),
            MarketData(
                currencyPair = "bch_krw",
                timestamp = 1691303226691,
                last = "300500",
                open = "297900",
                bid = "300800",
                ask = "301200",
                low = "296100",
                high = "301400",
                volume = "135.54887090",
                change = "-1",
                changePercent = "-1"
            )
        )
    )

    KorbitTestTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchTopBar()
            MarketTabScreen(marketDataPreprocessedDataList)
        }
    }
}