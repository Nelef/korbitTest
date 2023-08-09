package com.uyjang.korbittest.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme
import com.uyjang.korbittest.view.ui.theme.LightBlue
import com.uyjang.korbittest.view.ui.theme.LightRed
import com.uyjang.korbittest.viewModel.MarketData
import com.uyjang.korbittest.viewModel.MarketDataPreprocessedData
import com.uyjang.korbittest.viewModel.ShowMarketData

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    KorbitTestTheme {
        SearchTopBar("") {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSortButton() {
    KorbitTestTheme {
        Row {
            SortButton(onClick = {}, text = "가상자산명")
            SortButton(onClick = {}, text = "현재가")
            SortButton(onClick = {}, text = "24시간")
            SortButton(onClick = {}, text = "거래대금")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMarketList() {
    // 프리뷰 테스트 코드
    val marketDataPreprocessedDataList: List<MarketDataPreprocessedData> = listOf(
        MarketDataPreprocessedData(
            ShowMarketData(
                currencyPair = "hnt/krw",
                lastPrice = "2,523",
                priceChangeRate = "+0.24%",
                priceChangePrice = "+6.00",
                tradeVolume = "45",
                favorite = false
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
                tradeVolume = "1,351",
                favorite = true
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
            MarketTabScreen(
                marketDataPreprocessedDataList,
                marketDataPreprocessedDataList,
                11,
                {}) {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    searchText: String,
    onSearchText: (searchText: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    KorbitMarketBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp, start = 15.dp, end = 15.dp, bottom = 3.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            }
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
            TextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(end = 5.dp),
                value = searchText,
                onValueChange = { onSearchText(it) },
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                placeholder = { Text(text = "검색", fontSize = 12.sp) },
                singleLine = true
            )
        }
    }
}

@Composable
fun MarketTabScreen(
    marketDataPreprocessedDataList: List<MarketDataPreprocessedData>,
    favoriteMarketDataPreprocessedDataList: List<MarketDataPreprocessedData>,
    sortButtonNum: Int,
    onClickSortButton: (sortButtonNum: Int) -> Unit,
    onFavoriteClick: (currencyPair: String) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("마켓", "즐겨찾기")
    var sortButtonNum by remember { mutableIntStateOf(sortButtonNum) }

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
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 23.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .size(25.dp)
                    .wrapContentSize(Alignment.Center),
                text = "즐겨\n찾기",
            )
            SortButton(
                onClick = {
                    when (sortButtonNum) {
                        !in 11..12 -> sortButtonNum = 11
                        11 -> sortButtonNum = 12
                        12 -> sortButtonNum = 11
                    }
                    onClickSortButton(sortButtonNum)
                },
                modifier = Modifier.weight(1.2f),
                contentAlign = Alignment.CenterStart,
                text = "가상자산명",
                currentSortNum = when (sortButtonNum) {
                    11 -> 1
                    12 -> 2
                    else -> 0
                }
            )
            SortButton(
                onClick = {
                    when (sortButtonNum) {
                        !in 21..22 -> sortButtonNum = 21
                        21 -> sortButtonNum = 22
                        22 -> sortButtonNum = 21
                    }
                    onClickSortButton(sortButtonNum)
                },
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterEnd,
                text = "현재가",
                currentSortNum = when (sortButtonNum) {
                    21 -> 1
                    22 -> 2
                    else -> 0
                }
            )
            SortButton(
                onClick = {
                    when (sortButtonNum) {
                        !in 31..32 -> sortButtonNum = 31
                        31 -> sortButtonNum = 32
                        32 -> sortButtonNum = 31
                    }
                    onClickSortButton(sortButtonNum)
                },
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterEnd,
                text = "24시간",
                currentSortNum = when (sortButtonNum) {
                    31 -> 1
                    32 -> 2
                    else -> 0
                }
            )
            SortButton(
                onClick = {
                    when (sortButtonNum) {
                        !in 41..42 -> sortButtonNum = 41
                        41 -> sortButtonNum = 42
                        42 -> sortButtonNum = 41
                    }
                    onClickSortButton(sortButtonNum)
                },
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterEnd,
                text = "거래대금",
                currentSortNum = when (sortButtonNum) {
                    41 -> 1
                    42 -> 2
                    else -> 0
                }
            )
        }
        Divider(
            Modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .height(1.dp)
        )
        when (tabIndex) {
            0 -> {
                MarketList(marketDataPreprocessedDataList) { currencyPair ->
                    onFavoriteClick(currencyPair)
                }
            }

            1 -> {
                MarketList(favoriteMarketDataPreprocessedDataList) { currencyPair ->
                    onFavoriteClick(currencyPair)
                }
            }
        }
    }
}

@Composable
fun SortButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    currentSortNum: Int = 0,
    contentAlign: Alignment = Alignment.CenterStart
) {
    var arrowUpTint = Color.Gray
    var arrowDownTint = Color.Gray
    when (currentSortNum) {
        1 -> {
            arrowUpTint = Color.Gray
            arrowDownTint = LocalContentColor.current
        }

        2 -> {
            arrowUpTint = LocalContentColor.current
            arrowDownTint = Color.Gray
        }

        else -> {
            arrowUpTint = Color.Gray
            arrowDownTint = Color.Gray
        }
    }
    val textStyle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    )

    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(color = Color.Transparent, shape = RoundedCornerShape(0.dp)),
        contentAlignment = contentAlign
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, style = textStyle)
            Column {
                val size = 23.dp
                val offset = 9.dp
                Icon(
                    modifier = Modifier
                        .size(size)
                        .offset(y = offset),
                    imageVector = Icons.Rounded.ArrowDropUp,
                    contentDescription = "ArrowDropUp Icon",
                    tint = arrowUpTint
                )
                Icon(
                    modifier = Modifier
                        .size(size)
                        .offset(y = -offset),
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = "ArrowDropDown Icon",
                    tint = arrowDownTint
                )
            }
        }
    }
}

@Composable
fun MarketList(
    marketDataPreprocessedDataList: List<MarketDataPreprocessedData>,
    onFavoriteClick: (currencyPair: String) -> Unit
) {
    LazyColumn(
        Modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
        }
        items(marketDataPreprocessedDataList) { marketDataPreprocessedData ->
            MarketDataRow(marketDataPreprocessedData) {
                onFavoriteClick(marketDataPreprocessedData.showMarketData.currencyPair)
            }
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun MarketDataRow(
    marketDataPreprocessedData: MarketDataPreprocessedData,
    onFavoriteClick: () -> Unit
) {
    val showMarketData = marketDataPreprocessedData.showMarketData
    KorbitMarketBox(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .height(50.dp)
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 즐겨찾기
            Icon(
                modifier = Modifier
                    .clickable { onFavoriteClick() }
                    .size(25.dp),
                imageVector = if (showMarketData.favorite) Icons.Rounded.Star else Icons.Rounded.StarOutline,
                contentDescription = "Favorite Icon"
            )

            // 가상자산명 : 해당 거래의 통화쌍 (BTC/KRW, ETH/KRW 같은 형식으로 표기)
            Text(modifier = Modifier.weight(1.2f), text = showMarketData.currencyPair)

            // 현재가 : 최종 체결 가격
            Text(
                text = showMarketData.lastPrice,
                modifier = Modifier.weight(1f), textAlign = TextAlign.End
            )

            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                val textColor = when {
                    marketDataPreprocessedData.originalMarketData.change.toDouble() > 0 -> LightRed
                    marketDataPreprocessedData.originalMarketData.change.toDouble() < 0 -> LightBlue
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