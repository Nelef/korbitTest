package com.uyjang.korbittest.view.fragment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.trace
import androidx.fragment.app.viewModels
import com.uyjang.korbittest.base.BaseFragment
import com.uyjang.korbittest.view.compose.KorbitBox
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
                    TopBar()
                    TabScreen(viewModel.marketDataPreprocessedDataList)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainFragment() {
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
            TopBar()
            TabScreen(marketDataPreprocessedDataList)
        }
    }
}

@Composable
fun TopBar() {
    KorbitBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                modifier = Modifier
                    .size(45.dp)
                    .padding(10.dp),
                tint = LocalContentColor.current
            )
            Text(text = "검색")
        }
    }
}

@Composable
fun TabScreen(marketDataPreprocessedDataList: List<MarketDataPreprocessedData>) {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("마켓", "즐겨찾기")

    Column {
        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            selectedTabIndex = tabIndex,
            contentColor = LocalContentColor.current, // 탭 텍스트 색상
            indicator = { tabPositions ->
                // 인디케이터 색상과 위치 설정
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                    color = LocalContentColor.current
                )
            },
            divider = {
                // 인디케이터 밑에 줄 설정
                Divider(Modifier.height(0.dp))
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        Divider(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
        )
        when (tabIndex) {
            0 -> MarketList(marketDataPreprocessedDataList)
            1 -> testList()
        }
    }
}

@Composable
fun MarketList(marketDataPreprocessedDataList: List<MarketDataPreprocessedData>) {
    LazyColumn(
        Modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(marketDataPreprocessedDataList) { marketDataPreprocessedData ->
            MarketDataRow(marketDataPreprocessedData)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MarketDataRow(marketDataPreprocessedData: MarketDataPreprocessedData) {
    val showMarketData = marketDataPreprocessedData.showMarketData
    KorbitBox(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .height(50.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 즐겨찾기
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = Icons.Default.Star,
                contentDescription = "Bookmark Icon",
                tint = LocalContentColor.current
            )

            // 가상자산명 : 해당 거래의 통화쌍 (BTC/KRW, ETH/KRW 같은 형식으로 표기)
            Text(modifier = Modifier.weight(1f), text = showMarketData.currencyPair)

            // 현재가 : 최종 체결 가격
            Text(
                text = showMarketData.lastPrice,
                modifier = Modifier.weight(1f), textAlign = TextAlign.End
            )

            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                val textColor = when {
                    marketDataPreprocessedData.originalMarketData.change.toDouble() > 0 -> Color.Red
                    marketDataPreprocessedData.originalMarketData.change.toDouble() < 0 -> Color.Blue
                    else -> LocalContentColor.current
                }
                // 변동률 : 시작 가격 대비 현재가 차이 변화 비율
                Text(
                    text = showMarketData.priceChangeRate,
                    color = textColor
                )

                // 변동가격 : 시작 가격 대비 현재가 차이
                Text(
                    text = showMarketData.priceChangePrice,
                    color = textColor
                )
            }

            // 거래대금 : 거래량
            Text(
                text = showMarketData.tradeVolume,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun testList() {
    // 데이터 리스트 생성
    val itemList = (1..1000).map { "Item $it" }

    // LazyColumn을 사용하여 리스트 생성
    LazyColumn(Modifier.fillMaxWidth()) {
        items(itemList) { item ->
            // 리스트 아이템 컴포넌트
            KorbitBox(
                Modifier
                    .fillMaxWidth()
                    .width(20.dp)
                    .padding(10.dp)
            ) {
                Text(text = item)
            }
        }
    }
}