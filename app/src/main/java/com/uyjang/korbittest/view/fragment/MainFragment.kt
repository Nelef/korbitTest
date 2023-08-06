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
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.uyjang.korbittest.base.BaseFragment
import com.uyjang.korbittest.view.compose.KorbitBox
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme
import com.uyjang.korbittest.viewModel.MainViewModel
import com.uyjang.korbittest.viewModel.MarketData
import kotlin.math.abs

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
                    TabScreen(viewModel.marketDataList)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainFragment() {
    val marketDataList: List<MarketData> = listOf(
        MarketData(
            currencyPair = "hnt_krw",
            timestamp = 1691301159586,
            last = "2900",
            open = "2900",
            bid = "2522",
            ask = "2900",
            low = "2488",
            high = "2900",
            volume = "246.04376246",
            change = "0",
            changePercent = "0"
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
            change = "2600",
            changePercent = "0.87"
        )
    )

    KorbitTestTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar()
            TabScreen(marketDataList)
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
fun TabScreen(marketDataList: List<MarketData>) {
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
            0 -> MarketList(marketDataList)
            1 -> MarketList(marketDataList)
        }
    }
}

@Composable
fun MarketList(marketDataList: List<MarketData>) {
    LazyColumn(
        Modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(marketDataList) { marketData ->
            MarketDataRow(marketData)
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MarketDataRow(marketData: MarketData) {
    KorbitBox(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(25.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Bookmark Icon",
                    modifier = Modifier.size(25.dp),
                    tint = LocalContentColor.current
                )
            }

            // 가상자산명 : 해당 거래의 통화쌍 (BTC/KRW, ETH/KRW 같은 형식으로 표기)
            val currencyPair = marketData.currencyPair.replace("_", "/")
            Box(modifier = Modifier.weight(1f)) {
                Text(text = currencyPair)
            }

            // 현재가 : 최종 체결 가격
            val lastPrice = marketData.last
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Text(text = lastPrice)
            }

            Column(modifier = Modifier.weight(1f)) {
                // 변동률 : 시작 가격 대비 현재가 차이 변화 비율
                val openingPrice = marketData.open.toDouble()
                val currentPrice = marketData.last.toDouble()
                val priceChangeRate =
                    String.format("%,.2f", (currentPrice - openingPrice) / openingPrice * 100)
                val textColor = when {
                    priceChangeRate.toDouble() > 0 -> Color.Red
                    priceChangeRate.toDouble() < 0 -> Color.Blue
                    else -> LocalContentColor.current
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(
                        text = "$priceChangeRate%",
                        color = textColor
                    )
                }

                // 변동가격 : 시작 가격 대비 현재가 차이
                val priceChange = formatPriceChange(openingPrice - currentPrice)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Text(text = "$priceChange", color = textColor)
                }
            }

            // 거래대금 : 거래량
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                Text(text = String.format("%,d", marketData.volume.toDouble().toInt()))
            }

//            Text(text = "최종 체결 시각: ${marketData.timestamp}")
//            Text(text = "시작 가격: ${marketData.open}")
//            Text(text = "매수호가: ${marketData.bid}")
//            Text(text = "매도호가: ${marketData.ask}")
//            Text(text = "최저가: ${marketData.low}")
//            Text(text = "최고가: ${marketData.high}")
//            Text(text = "거래량: ${marketData.volume}")
//            Text(text = "가격 변동: ${marketData.change}")
//            Text(text = "가격 변동 비율: ${marketData.changePercent}")
        }
    }
}

fun formatPriceChange(value: Double): String {
    return when {
        abs(value) >= 100 -> {
            String.format("%,d", value.toInt())
        }

        else -> {
            String.format("%,.2f", value)
        }
    }
}