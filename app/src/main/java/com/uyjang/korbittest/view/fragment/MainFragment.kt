package com.uyjang.korbittest.view.fragment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import com.uyjang.korbittest.base.BaseFragment
import com.uyjang.korbittest.view.compose.KorbitBox
import com.uyjang.korbittest.viewModel.MainViewModel
import com.uyjang.korbittest.viewModel.MarketData

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

@Composable
fun TopBar() {
    KorbitBox(modifier = Modifier.fillMaxWidth()) {
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
    LazyColumn {
        items(marketDataList) { marketData ->
            MarketDataRow(marketData)
        }
    }
}

@Composable
fun MarketDataRow(marketData: MarketData) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "통화쌍: ${marketData.currencyPair}")
        Text(text = "최종 체결 시각: ${marketData.timestamp}")
        Text(text = "최종 체결 가격: ${marketData.last}")
        Text(text = "시작 가격: ${marketData.open}")
        Text(text = "매수호가: ${marketData.bid}")
        Text(text = "매도호가: ${marketData.ask}")
        Text(text = "최저가: ${marketData.low}")
        Text(text = "최고가: ${marketData.high}")
        Text(text = "거래량: ${marketData.volume}")
        Text(text = "가격 변동: ${marketData.change}")
        Text(text = "가격 변동 비율: ${marketData.changePercent}")
    }
}