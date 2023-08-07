package com.uyjang.korbittest.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme
import com.uyjang.korbittest.viewModel.MarketData
import com.uyjang.korbittest.viewModel.MarketDataPreprocessedData
import com.uyjang.korbittest.viewModel.ShowMarketData

@Preview(showBackground = true)
@Composable
fun PreviewSearchBar() {
    KorbitTestTheme {
        SearchTopBar()
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
            MarketTabScreen(marketDataPreprocessedDataList) {}
        }
    }
}

@Composable
fun SearchTopBar() {
    KorbitMarketBox(
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
fun MarketTabScreen(
    marketDataPreprocessedDataList: List<MarketDataPreprocessedData>,
    onClickSortButton: (sortButtonNum: Int) -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("마켓", "즐겨찾기")
    val sortButtons by remember { mutableStateOf(List(4) { mutableStateOf(0) }) }

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
                    onClick = {
                        tabIndex = index
                        sortButtons.forEach { it.value = 0 }
                    }
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
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SortButton(
                onClick = {
                    sortButtons[0].value = (sortButtons[0].value % 2) + 1
                    sortButtons[1].value = 0
                    sortButtons[2].value = 0
                    sortButtons[3].value = 0
                    onClickSortButton(if (sortButtons[0].value == 1) 11 else 12)
                },
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterStart,
                text = "가상자산명",
                currentSortNum = sortButtons[0].value
            )
            Spacer(Modifier.size(25.dp))
            SortButton(
                onClick = {
                    sortButtons[0].value = 0
                    sortButtons[1].value = (sortButtons[1].value % 2) + 1
                    sortButtons[2].value = 0
                    sortButtons[3].value = 0
                    onClickSortButton(if (sortButtons[1].value == 1) 21 else 22)
                }, modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterEnd,
                text = "현재가",
                currentSortNum = sortButtons[1].value
            )
            SortButton(
                onClick = {
                    sortButtons[0].value = 0
                    sortButtons[1].value = 0
                    sortButtons[2].value = (sortButtons[2].value % 2) + 1
                    sortButtons[3].value = 0
                    onClickSortButton(if (sortButtons[2].value == 1) 31 else 32)
                },
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterEnd,
                text = "24시간",
                currentSortNum = sortButtons[2].value
            )
            SortButton(
                onClick = {
                    sortButtons[0].value = 0
                    sortButtons[1].value = 0
                    sortButtons[2].value = 0
                    sortButtons[3].value = (sortButtons[3].value % 2) + 1
                    onClickSortButton(if (sortButtons[3].value == 1) 41 else 42)
                },
                modifier = Modifier.weight(1f),
                contentAlign = Alignment.CenterEnd,
                text = "거래대금",
                currentSortNum = sortButtons[3].value
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
                MarketList(marketDataPreprocessedDataList)
            }

            1 -> {
                testList()
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
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = text, style = textStyle)
            Column {
                val size = 23.dp
                val offset = 9.dp
                Icon(
                    modifier = Modifier
                        .size(size)
                        .offset(y = offset),
                    imageVector = rememberArrowDropUp(),
                    contentDescription = "ArrowDropUp Icon",
                    tint = arrowUpTint
                )
                Icon(
                    modifier = Modifier
                        .size(size)
                        .offset(y = -offset),
                    imageVector = rememberArrowDropDown(),
                    contentDescription = "ArrowDropDown Icon",
                    tint = arrowDownTint
                )
            }
        }
    }
}

@Composable
fun rememberArrowDropUp(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "arrow_drop_up",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11.875f, 23.208f)
                lineTo(20f, 15.083f)
                lineToRelative(8.125f, 8.125f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberArrowDropDown(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "arrow_drop_down",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 24.875f)
                lineToRelative(-8.125f, -8.083f)
                horizontalLineToRelative(16.25f)
                close()
            }
        }.build()
    }
}

@Composable
fun MarketList(marketDataPreprocessedDataList: List<MarketDataPreprocessedData>) {
    LazyColumn(
        Modifier.padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
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
            KorbitMarketBox(
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