package com.uyjang.korbittest.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uyjang.korbittest.data.internal.FavoriteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var searchText by mutableStateOf("")

    // 검색, 정렬을 위해 복구용 백업 리스트
    private var tempMarketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())
    private var tempFavoriteMarketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())

    // 마켓 리스트
    var marketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())

    // 즐겨찾기 리스트
    var favoriteMarketDataPreprocessedDataList by mutableStateOf(emptyList<MarketDataPreprocessedData>())

    val favoriteDataSource = FavoriteDataSource(getApplication())

    // 즐겨찾기 목록
    var favorites by mutableStateOf(emptyList<String>())

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
        }
    }

    fun getFavorites(): Flow<List<String>> {
        return favoriteDataSource.getPreference()
    }

    init {
        // 코루틴을 사용하여 getPreference의 Flow 값을 가져온 후 StateFlow에 설정
        viewModelScope.launch {
            initMarketData()
        }
    }

    private suspend fun initMarketData() {
        favorites = getFavorites().first()
        Log.d("uyTest", favorites.toString())
        val jsonData = """
                {
                    "hnt_krw": {
                        "timestamp": 1691359443184,
                        "last": "2523",
                        "open": "2517",
                        "bid": "2525",
                        "ask": "2894",
                        "low": "2523",
                        "high": "2900",
                        "volume": "45.57676204",
                        "change": "6",
                        "changePercent": "0.24"
                    },
                    "bch_krw": {
                        "timestamp": 1691383050843,
                        "last": "308100",
                        "open": "300200",
                        "bid": "307000",
                        "ask": "308500",
                        "low": "289900",
                        "high": "309500",
                        "volume": "109.65998455",
                        "change": "7900",
                        "changePercent": "2.63"
                    },
                    "fet_krw": {
                        "timestamp": 1691379493099,
                        "last": "260.8",
                        "open": "257.7",
                        "bid": "246.5",
                        "ask": "263.6",
                        "low": "257.4",
                        "high": "261.6",
                        "volume": "3390.836758357852760736",
                        "change": "3.1",
                        "changePercent": "1.20"
                    },
                    "theta_krw": {
                        "timestamp": 1691368002085,
                        "last": "974.9",
                        "open": "981.9",
                        "bid": "956.3",
                        "ask": "965.4",
                        "low": "972.9",
                        "high": "981.4",
                        "volume": "380.224991",
                        "change": "-7.0",
                        "changePercent": "-0.71"
                    },
                    "blur_krw": {
                        "timestamp": 1691373422133,
                        "last": "402.6",
                        "open": "400",
                        "bid": "396.5",
                        "ask": "413.5",
                        "low": "397.4",
                        "high": "402.6",
                        "volume": "3607.834723410000000000",
                        "change": "2.6",
                        "changePercent": "0.65"
                    },
                    "xtz_krw": {
                        "timestamp": 1691381870158,
                        "last": "1074",
                        "open": "1071",
                        "bid": "1063",
                        "ask": "1072",
                        "low": "1064",
                        "high": "1074",
                        "volume": "837.807197",
                        "change": "3",
                        "changePercent": "0.28"
                    },
                    "1inch_krw": {
                        "timestamp": 1691375320213,
                        "last": "408.5",
                        "open": "406.2",
                        "bid": "405",
                        "ask": "406.3",
                        "low": "404.3",
                        "high": "411.8",
                        "volume": "5044.928136200000000000",
                        "change": "2.3",
                        "changePercent": "0.57"
                    },
                    "ygg_krw": {
                        "timestamp": 1691381481186,
                        "last": "780",
                        "open": "611.3",
                        "bid": "780.1",
                        "ask": "785.9",
                        "low": "608.5",
                        "high": "899.9",
                        "volume": "93138.999560708405960125",
                        "change": "168.7",
                        "changePercent": "27.60"
                    },
                    "yfi_krw": {
                        "timestamp": 1691298700736,
                        "last": "8470000",
                        "open": "8406000",
                        "bid": "8542000",
                        "ask": "8629000",
                        "low": "8376000",
                        "high": "8470000",
                        "volume": "0.062966640000000000",
                        "change": "64000",
                        "changePercent": "0.76"
                    },
                    "alpha_krw": {
                        "timestamp": 1691339715636,
                        "last": "136.9",
                        "open": "133.2",
                        "bid": "137.1",
                        "ask": "137.6",
                        "low": "136.9",
                        "high": "136.9",
                        "volume": "907.815477840000000000",
                        "change": "3.7",
                        "changePercent": "2.78"
                    },
                    "bnb_krw": {
                        "timestamp": 1691374528431,
                        "last": "322100",
                        "open": "325000",
                        "bid": "322700",
                        "ask": "322800",
                        "low": "322100",
                        "high": "325000",
                        "volume": "34.40903051",
                        "change": "-2900",
                        "changePercent": "-0.89"
                    },
                    "gal_krw": {
                        "timestamp": 1691322593819,
                        "last": "1601",
                        "open": "1540",
                        "bid": "1639",
                        "ask": "1649",
                        "low": "1585",
                        "high": "1601",
                        "volume": "600.000000000000000000",
                        "change": "61",
                        "changePercent": "3.96"
                    },
                    "mnt_krw": {
                        "timestamp": 1691381007866,
                        "last": "729.6",
                        "open": "687.5",
                        "bid": "600",
                        "ask": "729.6",
                        "low": "552.3",
                        "high": "797.9",
                        "volume": "1591.947044155200520689",
                        "change": "42.1",
                        "changePercent": "6.12"
                    },
                    "xpla_krw": {
                        "timestamp": 1691382912477,
                        "last": "374",
                        "open": "376",
                        "bid": "373",
                        "ask": "374",
                        "low": "363.9",
                        "high": "405.8",
                        "volume": "52605.623040416801461572",
                        "change": "-2",
                        "changePercent": "-0.53"
                    },
                    "avax_krw": {
                        "timestamp": 1691379880716,
                        "last": "16860",
                        "open": "16650",
                        "bid": "16810",
                        "ask": "16910",
                        "low": "16660",
                        "high": "16860",
                        "volume": "59.412301067359123967",
                        "change": "210",
                        "changePercent": "1.26"
                    },
                    "npt_krw": {
                        "timestamp": 1691372324597,
                        "last": "608.1",
                        "open": "647.9",
                        "bid": "609",
                        "ask": "635.9",
                        "low": "608.1",
                        "high": "645.9",
                        "volume": "375.883600000000000000",
                        "change": "-39.8",
                        "changePercent": "-6.14"
                    },
                    "sui_krw": {
                        "timestamp": 1691379115778,
                        "last": "787.6",
                        "open": "794.5",
                        "bid": "789.3",
                        "ask": "790.2",
                        "low": "782",
                        "high": "805.8",
                        "volume": "82719.228775579",
                        "change": "-6.9",
                        "changePercent": "-0.87"
                    },
                    "rose_krw": {
                        "timestamp": 1691381638321,
                        "last": "62.67",
                        "open": "62.81",
                        "bid": "62.65",
                        "ask": "62.85",
                        "low": "62.67",
                        "high": "62.67",
                        "volume": "81.378650072",
                        "change": "-0.14",
                        "changePercent": "-0.22"
                    },
                    "sol_krw": {
                        "timestamp": 1691382250553,
                        "last": "30900",
                        "open": "30340",
                        "bid": "30860",
                        "ask": "30900",
                        "low": "30400",
                        "high": "31210",
                        "volume": "487.583901706",
                        "change": "560",
                        "changePercent": "1.85"
                    },
                    "mana_krw": {
                        "timestamp": 1691375346708,
                        "last": "499.3",
                        "open": "495.7",
                        "bid": "498.7",
                        "ask": "499.3",
                        "low": "492.7",
                        "high": "502",
                        "volume": "4061.705843054980079681",
                        "change": "3.6",
                        "changePercent": "0.73"
                    },
                    "rare_krw": {
                        "timestamp": 1690885667526,
                        "last": "95",
                        "open": "95",
                        "bid": "94.72",
                        "ask": "98.57",
                        "low": "95",
                        "high": "95",
                        "volume": "0.000000000000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "gtc_krw": {
                        "timestamp": 1691382392422,
                        "last": "1564",
                        "open": "1403",
                        "bid": "1564",
                        "ask": "1569",
                        "low": "1413",
                        "high": "1950",
                        "volume": "33657.762653022086667445",
                        "change": "161",
                        "changePercent": "11.48"
                    },
                    "dai_krw": {
                        "timestamp": 1691191631224,
                        "last": "1337",
                        "open": "1337",
                        "bid": "1320",
                        "ask": "1339",
                        "low": "1337",
                        "high": "1337",
                        "volume": "0.000000000000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "zil_krw": {
                        "timestamp": 1691380869248,
                        "last": "26.8",
                        "open": "27.11",
                        "bid": "26.79",
                        "ask": "26.83",
                        "low": "26.67",
                        "high": "27.12",
                        "volume": "177498.026589060000",
                        "change": "-0.31",
                        "changePercent": "-1.14"
                    },
                    "cvx_krw": {
                        "timestamp": 1691380204773,
                        "last": "5000",
                        "open": "4746",
                        "bid": "4339",
                        "ask": "5000",
                        "low": "4245",
                        "high": "5085",
                        "volume": "10.219224027885939036",
                        "change": "254",
                        "changePercent": "5.35"
                    },
                    "gmx_krw": {
                        "timestamp": 1691375977383,
                        "last": "71100",
                        "open": "70500",
                        "bid": "71200",
                        "ask": "71350",
                        "low": "70550",
                        "high": "71900",
                        "volume": "1.999927305640000000",
                        "change": "600",
                        "changePercent": "0.85"
                    },
                    "flow_krw": {
                        "timestamp": 1691377038953,
                        "last": "740",
                        "open": "743.4",
                        "bid": "736.6",
                        "ask": "742",
                        "low": "734.9",
                        "high": "743.4",
                        "volume": "2280.14806304",
                        "change": "-3.4",
                        "changePercent": "-0.46"
                    },
                    "audio_krw": {
                        "timestamp": 1691371468206,
                        "last": "259.1",
                        "open": "231.7",
                        "bid": "241.2",
                        "ask": "243.2",
                        "low": "231.8",
                        "high": "259.1",
                        "volume": "32588.760767837194000000",
                        "change": "27.4",
                        "changePercent": "11.83"
                    },
                    "uni_krw": {
                        "timestamp": 1691368561080,
                        "last": "7855",
                        "open": "8010",
                        "bid": "7880",
                        "ask": "7950",
                        "low": "7855",
                        "high": "8030",
                        "volume": "152.215195330000000000",
                        "change": "-155",
                        "changePercent": "-1.94"
                    },
                    "wld_krw": {
                        "timestamp": 1691380773971,
                        "last": "2727",
                        "open": "2820",
                        "bid": "2727",
                        "ask": "2729",
                        "low": "2666",
                        "high": "2831",
                        "volume": "26346.601320260211539335",
                        "change": "-93",
                        "changePercent": "-3.30"
                    },
                    "mina_krw": {
                        "timestamp": 1691377383539,
                        "last": "581.6",
                        "open": "582",
                        "bid": "580.8",
                        "ask": "582.3",
                        "low": "576",
                        "high": "581.6",
                        "volume": "11847.780768478",
                        "change": "-0.4",
                        "changePercent": "-0.07"
                    },
                    "klay_krw": {
                        "timestamp": 1691382310883,
                        "last": "207.8",
                        "open": "205.8",
                        "bid": "207.8",
                        "ask": "208.1",
                        "low": "205.8",
                        "high": "208.5",
                        "volume": "446905.515283902606700273",
                        "change": "2.0",
                        "changePercent": "0.97"
                    },
                    "ksm_krw": {
                        "timestamp": 1691381357518,
                        "last": "29700",
                        "open": "28550",
                        "bid": "29510",
                        "ask": "29590",
                        "low": "29080",
                        "high": "29900",
                        "volume": "340.754243446700",
                        "change": "1150",
                        "changePercent": "4.03"
                    },
                    "wbtc_krw": {
                        "timestamp": 1691018833931,
                        "last": "37479000",
                        "open": "37479000",
                        "bid": "37674000",
                        "ask": "38776000",
                        "low": "37479000",
                        "high": "37479000",
                        "volume": "0.00000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "bora_krw": {
                        "timestamp": 1691373271823,
                        "last": "191.1",
                        "open": "186.7",
                        "bid": "186.8",
                        "ask": "191.1",
                        "low": "191.1",
                        "high": "192.1",
                        "volume": "704.266829581749089016",
                        "change": "4.4",
                        "changePercent": "2.36"
                    },
                    "mkr_krw": {
                        "timestamp": 1691360266765,
                        "last": "1625000",
                        "open": "1659000",
                        "bid": "1629000",
                        "ask": "1638000",
                        "low": "1622000",
                        "high": "1641000",
                        "volume": "1.193800000000000000",
                        "change": "-34000",
                        "changePercent": "-2.05"
                    },
                    "xlm_krw": {
                        "timestamp": 1691382971740,
                        "last": "184.9",
                        "open": "186.1",
                        "bid": "184.7",
                        "ask": "184.8",
                        "low": "184",
                        "high": "191.8",
                        "volume": "275306.0052262",
                        "change": "-1.2",
                        "changePercent": "-0.64"
                    },
                    "usdc_krw": {
                        "timestamp": 1691377790106,
                        "last": "1325",
                        "open": "1325",
                        "bid": "1322",
                        "ask": "1325",
                        "low": "1325",
                        "high": "1330",
                        "volume": "9429.057728",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "dydx_krw": {
                        "timestamp": 1691281612889,
                        "last": "2738",
                        "open": "2738",
                        "bid": "2714",
                        "ask": "2716",
                        "low": "2738",
                        "high": "2738",
                        "volume": "0.000000000000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "axs_krw": {
                        "timestamp": 1691380317966,
                        "last": "7870",
                        "open": "7735",
                        "bid": "7780",
                        "ask": "7870",
                        "low": "7735",
                        "high": "7870",
                        "volume": "78.218646800000000000",
                        "change": "135",
                        "changePercent": "1.75"
                    },
                    "gmt_krw": {
                        "timestamp": 1691382667241,
                        "last": "275.8",
                        "open": "275",
                        "bid": "272.6",
                        "ask": "275.2",
                        "low": "272",
                        "high": "275.8",
                        "volume": "12777.164653270",
                        "change": "0.8",
                        "changePercent": "0.29"
                    },
                    "uma_krw": {
                        "timestamp": 1691380840972,
                        "last": "2122",
                        "open": "2095",
                        "bid": "2132",
                        "ask": "2177",
                        "low": "2122",
                        "high": "2184",
                        "volume": "2360.121848610000000000",
                        "change": "27",
                        "changePercent": "1.29"
                    },
                    "hbar_krw": {
                        "timestamp": 1691379719644,
                        "last": "73.62",
                        "open": "72",
                        "bid": "73.43",
                        "ask": "73.61",
                        "low": "71.5",
                        "high": "79.07",
                        "volume": "494889.89907333",
                        "change": "1.62",
                        "changePercent": "2.25"
                    },
                    "sgb_krw": {
                        "timestamp": 1691369286773,
                        "last": "9.183",
                        "open": "8.968",
                        "bid": "8.709",
                        "ask": "9.182",
                        "low": "8.611",
                        "high": "9.183",
                        "volume": "434107.580073560000000000",
                        "change": "0.215",
                        "changePercent": "2.40"
                    },
                    "looks_krw": {
                        "timestamp": 1691340777700,
                        "last": "76.98",
                        "open": "76.53",
                        "bid": "68.3",
                        "ask": "81.87",
                        "low": "76.12",
                        "high": "76.98",
                        "volume": "5686.619575634381657573",
                        "change": "0.45",
                        "changePercent": "0.59"
                    },
                    "med_krw": {
                        "timestamp": 1691377746110,
                        "last": "16.33",
                        "open": "16.1",
                        "bid": "16.35",
                        "ask": "16.87",
                        "low": "15.9",
                        "high": "16.93",
                        "volume": "1475945.592900",
                        "change": "0.23",
                        "changePercent": "1.43"
                    },
                    "index_krw": {
                        "timestamp": 1691380172365,
                        "last": "2067",
                        "open": "2106",
                        "bid": "1620",
                        "ask": "2067",
                        "low": "1577",
                        "high": "2087",
                        "volume": "118.373278510872065165",
                        "change": "-39",
                        "changePercent": "-1.85"
                    },
                    "icp_krw": {
                        "timestamp": 1691372334052,
                        "last": "5505",
                        "open": "5345",
                        "bid": "5490",
                        "ask": "5510",
                        "low": "5360",
                        "high": "5505",
                        "volume": "1722.99559023",
                        "change": "160",
                        "changePercent": "2.99"
                    },
                    "algo_krw": {
                        "timestamp": 1691382708729,
                        "last": "146.9",
                        "open": "142.5",
                        "bid": "146.6",
                        "ask": "146.9",
                        "low": "141.8",
                        "high": "146.9",
                        "volume": "53196.471860",
                        "change": "4.4",
                        "changePercent": "3.09"
                    },
                    "btc_krw": {
                        "timestamp": 1691382498344,
                        "last": "38567000",
                        "open": "38532000",
                        "bid": "38567000",
                        "ask": "38580000",
                        "low": "38490000",
                        "high": "38729000",
                        "volume": "15.31354247",
                        "change": "35000",
                        "changePercent": "0.09"
                    },
                    "bat_krw": {
                        "timestamp": 1691376831058,
                        "last": "286.6",
                        "open": "276.1",
                        "bid": "287.7",
                        "ask": "290",
                        "low": "279.9",
                        "high": "288.2",
                        "volume": "259.967455850000000000",
                        "change": "10.5",
                        "changePercent": "3.80"
                    },
                    "lrc_krw": {
                        "timestamp": 1691377552964,
                        "last": "303",
                        "open": "282.5",
                        "bid": "299.3",
                        "ask": "301",
                        "low": "290.6",
                        "high": "303",
                        "volume": "7044.841519410000000000",
                        "change": "20.5",
                        "changePercent": "7.26"
                    },
                    "rly_krw": {
                        "timestamp": 1691330876958,
                        "last": "8.993",
                        "open": "8.994",
                        "bid": "8.165",
                        "ask": "8.992",
                        "low": "8.993",
                        "high": "8.993",
                        "volume": "2085.115888860000000000",
                        "change": "-0.001",
                        "changePercent": "-0.01"
                    },
                    "dot_krw": {
                        "timestamp": 1691377910056,
                        "last": "6655",
                        "open": "6625",
                        "bid": "6635",
                        "ask": "6640",
                        "low": "6625",
                        "high": "6675",
                        "volume": "27.0119437804",
                        "change": "30",
                        "changePercent": "0.45"
                    },
                    "vet_krw": {
                        "timestamp": 1691380871422,
                        "last": "23.62",
                        "open": "23.78",
                        "bid": "23.65",
                        "ask": "23.75",
                        "low": "23.45",
                        "high": "23.96",
                        "volume": "429608.206298485979857323",
                        "change": "-0.16",
                        "changePercent": "-0.67"
                    },
                    "gala_krw": {
                        "timestamp": 1691377246633,
                        "last": "31.61",
                        "open": "32.75",
                        "bid": "31.35",
                        "ask": "31.39",
                        "low": "31.03",
                        "high": "33.35",
                        "volume": "955198.64407470",
                        "change": "-1.14",
                        "changePercent": "-3.48"
                    },
                    "perp_krw": {
                        "timestamp": 1691367802112,
                        "last": "672.7",
                        "open": "662.8",
                        "bid": "670.1",
                        "ask": "679.4",
                        "low": "663",
                        "high": "672.7",
                        "volume": "130.288232030000000000",
                        "change": "9.9",
                        "changePercent": "1.49"
                    },
                    "arb_krw": {
                        "timestamp": 1691330985238,
                        "last": "1507",
                        "open": "1505",
                        "bid": "1517",
                        "ask": "1521",
                        "low": "1507",
                        "high": "1515",
                        "volume": "18.346000370000000000",
                        "change": "2",
                        "changePercent": "0.13"
                    },
                    "enj_krw": {
                        "timestamp": 1691378441247,
                        "last": "385.5",
                        "open": "383.6",
                        "bid": "385.3",
                        "ask": "385.4",
                        "low": "379.5",
                        "high": "385.5",
                        "volume": "22182.061089540000000000",
                        "change": "1.9",
                        "changePercent": "0.50"
                    },
                    "osmo_krw": {
                        "timestamp": 1691278319273,
                        "last": "681.1",
                        "open": "681.1",
                        "bid": "591.2",
                        "ask": "676.6",
                        "low": "681.1",
                        "high": "681.1",
                        "volume": "0.000000",
                        "change": "0.0",
                        "changePercent": "0"
                    },
                    "rpl_krw": {
                        "timestamp": 1691314634614,
                        "last": "43440",
                        "open": "43670",
                        "bid": "36060",
                        "ask": "43290",
                        "low": "42690",
                        "high": "43440",
                        "volume": "1.148633126579370000",
                        "change": "-230",
                        "changePercent": "-0.53"
                    },
                    "ren_krw": {
                        "timestamp": 1691371888650,
                        "last": "79.93",
                        "open": "77.44",
                        "bid": "79.81",
                        "ask": "80.61",
                        "low": "79.51",
                        "high": "80.29",
                        "volume": "9513.072716019216589862",
                        "change": "2.49",
                        "changePercent": "3.22"
                    },
                    "ar_krw": {
                        "timestamp": 1691326806386,
                        "last": "6995",
                        "open": "6995",
                        "bid": "6995",
                        "ask": "7000",
                        "low": "6995",
                        "high": "7020",
                        "volume": "16.810639590000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "ohm_krw": {
                        "timestamp": 1689940586920,
                        "last": "14170",
                        "open": "14170",
                        "bid": "11850",
                        "ask": "14170",
                        "low": "14170",
                        "high": "14170",
                        "volume": "0.000000000000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "shib_krw": {
                        "timestamp": 1691382297431,
                        "last": "0.0123",
                        "open": "0.0126",
                        "bid": "0.0122",
                        "ask": "0.0123",
                        "low": "0.0122",
                        "high": "0.013",
                        "volume": "7756044025.104831270506163126",
                        "change": "-0.0003",
                        "changePercent": "-2.38"
                    },
                    "aave_krw": {
                        "timestamp": 1691380099517,
                        "last": "88050",
                        "open": "87000",
                        "bid": "87850",
                        "ask": "88800",
                        "low": "86650",
                        "high": "88050",
                        "volume": "13.231805460000000000",
                        "change": "1050",
                        "changePercent": "1.21"
                    },
                    "ada_krw": {
                        "timestamp": 1691380988393,
                        "last": "389",
                        "open": "389.7",
                        "bid": "389.3",
                        "ask": "389.5",
                        "low": "386.7",
                        "high": "391.2",
                        "volume": "10936.513697",
                        "change": "-0.7",
                        "changePercent": "-0.18"
                    },
                    "ksp_krw": {
                        "timestamp": 1691336467592,
                        "last": "847.7",
                        "open": "847.7",
                        "bid": "711.2",
                        "ask": "847.6",
                        "low": "710",
                        "high": "847.7",
                        "volume": "460.300063435944319925",
                        "change": "0.0",
                        "changePercent": "0"
                    },
                    "snx_krw": {
                        "timestamp": 1691380958030,
                        "last": "3447",
                        "open": "3425",
                        "bid": "3450",
                        "ask": "3483",
                        "low": "3360",
                        "high": "3490",
                        "volume": "2479.936779500000000000",
                        "change": "22",
                        "changePercent": "0.64"
                    },
                    "chz_krw": {
                        "timestamp": 1691382439426,
                        "last": "102.5",
                        "open": "101.4",
                        "bid": "102.4",
                        "ask": "102.7",
                        "low": "101.6",
                        "high": "102.8",
                        "volume": "19883.382363130000000000",
                        "change": "1.1",
                        "changePercent": "1.08"
                    },
                    "amp_krw": {
                        "timestamp": 1691380242582,
                        "last": "3.626",
                        "open": "3.674",
                        "bid": "3.198",
                        "ask": "3.626",
                        "low": "3.174",
                        "high": "3.632",
                        "volume": "16899.887806704011993654",
                        "change": "-0.048",
                        "changePercent": "-1.31"
                    },
                    "tdrop_krw": {
                        "timestamp": 1691378862797,
                        "last": "1.728",
                        "open": "1.725",
                        "bid": "1.728",
                        "ask": "1.824",
                        "low": "1.726",
                        "high": "1.728",
                        "volume": "32785.272946530000000000",
                        "change": "0.003",
                        "changePercent": "0.17"
                    },
                    "comp_krw": {
                        "timestamp": 1691368531962,
                        "last": "75950",
                        "open": "77900",
                        "bid": "75350",
                        "ask": "76150",
                        "low": "75950",
                        "high": "77550",
                        "volume": "3.600899614145077720",
                        "change": "-1950",
                        "changePercent": "-2.50"
                    },
                    "stx_krw": {
                        "timestamp": 1691381170725,
                        "last": "771.5",
                        "open": "773.2",
                        "bid": "771.5",
                        "ask": "771.9",
                        "low": "761.6",
                        "high": "771.5",
                        "volume": "810.519430",
                        "change": "-1.7",
                        "changePercent": "-0.22"
                    },
                    "ape_krw": {
                        "timestamp": 1691375591784,
                        "last": "2734",
                        "open": "2728",
                        "bid": "2504",
                        "ask": "2733",
                        "low": "2454",
                        "high": "2736",
                        "volume": "3341.513371807851962891",
                        "change": "6",
                        "changePercent": "0.22"
                    },
                    "aergo_krw": {
                        "timestamp": 1691356279634,
                        "last": "142.8",
                        "open": "135.2",
                        "bid": "141.8",
                        "ask": "144.3",
                        "low": "142.8",
                        "high": "142.8",
                        "volume": "350.140056020000000000",
                        "change": "7.6",
                        "changePercent": "5.62"
                    },
                    "gno_krw": {
                        "timestamp": 1691105235205,
                        "last": "140300",
                        "open": "140300",
                        "bid": "140200",
                        "ask": "148300",
                        "low": "140300",
                        "high": "140300",
                        "volume": "0.000000000000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "bsv_krw": {
                        "timestamp": 1691379887185,
                        "last": "47610",
                        "open": "47470",
                        "bid": "47180",
                        "ask": "48230",
                        "low": "47240",
                        "high": "48470",
                        "volume": "1.84024846",
                        "change": "140",
                        "changePercent": "0.29"
                    },
                    "btt_krw": {
                        "timestamp": 1691381515452,
                        "last": "0.0007",
                        "open": "0.0007",
                        "bid": "0.0006",
                        "ask": "0.0007",
                        "low": "0.0006",
                        "high": "0.0007",
                        "volume": "907227802.857142820000000000",
                        "change": "0.0000",
                        "changePercent": "0"
                    },
                    "crv_krw": {
                        "timestamp": 1691383076321,
                        "last": "814.7",
                        "open": "826.6",
                        "bid": "812.7",
                        "ask": "814.7",
                        "low": "808",
                        "high": "822.2",
                        "volume": "11504.928968300000000000",
                        "change": "-11.9",
                        "changePercent": "-1.44"
                    },
                    "band_krw": {
                        "timestamp": 1691378532467,
                        "last": "1639",
                        "open": "1613",
                        "bid": "1629",
                        "ask": "1635",
                        "low": "1601",
                        "high": "1639",
                        "volume": "6157.600200000000000000",
                        "change": "26",
                        "changePercent": "1.61"
                    },
                    "waxp_krw": {
                        "timestamp": 1691368581942,
                        "last": "65.71",
                        "open": "71.82",
                        "bid": "65.69",
                        "ask": "71.81",
                        "low": "65.71",
                        "high": "65.71",
                        "volume": "98.94993151",
                        "change": "-6.11",
                        "changePercent": "-8.51"
                    },
                    "doge_krw": {
                        "timestamp": 1691381554693,
                        "last": "99.6",
                        "open": "100.3",
                        "bid": "99.46",
                        "ask": "99.7",
                        "low": "98.49",
                        "high": "100.5",
                        "volume": "1078774.90892131",
                        "change": "-0.7",
                        "changePercent": "-0.70"
                    },
                    "pepe_krw": {
                        "timestamp": 1691382846337,
                        "last": "0.0016",
                        "open": "0.0016",
                        "bid": "0.0015",
                        "ask": "0.0016",
                        "low": "0.0015",
                        "high": "0.0016",
                        "volume": "1323451687.217997170000000000",
                        "change": "0.0000",
                        "changePercent": "0"
                    },
                    "eth_krw": {
                        "timestamp": 1691382369760,
                        "last": "2432000",
                        "open": "2433000",
                        "bid": "2432000",
                        "ask": "2434000",
                        "low": "2425000",
                        "high": "2438000",
                        "volume": "129.39236378",
                        "change": "-1000",
                        "changePercent": "-0.04"
                    },
                    "kda_krw": {
                        "timestamp": 1691378215404,
                        "last": "830.7",
                        "open": "702.7",
                        "bid": "704",
                        "ask": "823.5",
                        "low": "702.8",
                        "high": "834.5",
                        "volume": "1775.183034434236",
                        "change": "128.0",
                        "changePercent": "18.22"
                    },
                    "grt_krw": {
                        "timestamp": 1691377282589,
                        "last": "140",
                        "open": "140.3",
                        "bid": "139.3",
                        "ask": "139.8",
                        "low": "138.8",
                        "high": "140",
                        "volume": "3814.289766497379000000",
                        "change": "-0.3",
                        "changePercent": "-0.21"
                    },
                    "sand_krw": {
                        "timestamp": 1691382925722,
                        "last": "540.4",
                        "open": "541.3",
                        "bid": "540.7",
                        "ask": "542.2",
                        "low": "533.9",
                        "high": "543.1",
                        "volume": "7487.138773421847745750",
                        "change": "-0.9",
                        "changePercent": "-0.17"
                    },
                    "matic_krw": {
                        "timestamp": 1691379623307,
                        "last": "898.1",
                        "open": "887.9",
                        "bid": "895.8",
                        "ask": "898.1",
                        "low": "882.2",
                        "high": "900",
                        "volume": "2746.046093809870468535",
                        "change": "10.2",
                        "changePercent": "1.15"
                    },
                    "inj_krw": {
                        "timestamp": 1691375865246,
                        "last": "10590",
                        "open": "10590",
                        "bid": "10540",
                        "ask": "10620",
                        "low": "10590",
                        "high": "10590",
                        "volume": "51.692379540000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "ron_krw": {
                        "timestamp": 1691380986200,
                        "last": "942",
                        "open": "943.7",
                        "bid": "850.2",
                        "ask": "942",
                        "low": "850.1",
                        "high": "942",
                        "volume": "55.666499621917197452",
                        "change": "-1.7",
                        "changePercent": "-0.18"
                    },
                    "ldo_krw": {
                        "timestamp": 1691243289450,
                        "last": "2363",
                        "open": "2363",
                        "bid": "2357",
                        "ask": "2455",
                        "low": "2363",
                        "high": "2363",
                        "volume": "0.000000000000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "egld_krw": {
                        "timestamp": 1691376603573,
                        "last": "41290",
                        "open": "41650",
                        "bid": "41150",
                        "ask": "41240",
                        "low": "41240",
                        "high": "41300",
                        "volume": "63.082765760000000000",
                        "change": "-360",
                        "changePercent": "-0.86"
                    },
                    "eos_krw": {
                        "timestamp": 1691382499310,
                        "last": "972.8",
                        "open": "967",
                        "bid": "971.4",
                        "ask": "973.4",
                        "low": "961",
                        "high": "978.9",
                        "volume": "27768.6321",
                        "change": "5.8",
                        "changePercent": "0.60"
                    },
                    "xrp_krw": {
                        "timestamp": 1691382769205,
                        "last": "829.9",
                        "open": "845.6",
                        "bid": "829.4",
                        "ask": "829.9",
                        "low": "824",
                        "high": "844.8",
                        "volume": "1099215.685726",
                        "change": "-15.7",
                        "changePercent": "-1.86"
                    },
                    "ftm_krw": {
                        "timestamp": 1691375084662,
                        "last": "318.6",
                        "open": "311.1",
                        "bid": "315.7",
                        "ask": "319.4",
                        "low": "318.2",
                        "high": "318.6",
                        "volume": "54.713387806411062225",
                        "change": "7.5",
                        "changePercent": "2.41"
                    },
                    "lpt_krw": {
                        "timestamp": 1691383062358,
                        "last": "6970",
                        "open": "5225",
                        "bid": "6565",
                        "ask": "6970",
                        "low": "5295",
                        "high": "6970",
                        "volume": "3657.883258207775365916",
                        "change": "1745",
                        "changePercent": "33.40"
                    },
                    "near_krw": {
                        "timestamp": 1691363610307,
                        "last": "1774",
                        "open": "1805",
                        "bid": "1789",
                        "ask": "1804",
                        "low": "1774",
                        "high": "1815",
                        "volume": "128.374052136578818457000000",
                        "change": "-31",
                        "changePercent": "-1.72"
                    },
                    "link_krw": {
                        "timestamp": 1691380791287,
                        "last": "9485",
                        "open": "9560",
                        "bid": "9460",
                        "ask": "9490",
                        "low": "9415",
                        "high": "9490",
                        "volume": "83.001598441005291005",
                        "change": "-75",
                        "changePercent": "-0.78"
                    },
                    "qtum_krw": {
                        "timestamp": 1691382677053,
                        "last": "3422",
                        "open": "3422",
                        "bid": "3414",
                        "ask": "3418",
                        "low": "3388",
                        "high": "3447",
                        "volume": "2531.629995080000000000",
                        "change": "0",
                        "changePercent": "0"
                    },
                    "atom_krw": {
                        "timestamp": 1691361281793,
                        "last": "11320",
                        "open": "11220",
                        "bid": "11300",
                        "ask": "11360",
                        "low": "11210",
                        "high": "11340",
                        "volume": "192.614340",
                        "change": "100",
                        "changePercent": "0.89"
                    },
                    "syn_krw": {
                        "timestamp": 1691293109337,
                        "last": "763.5",
                        "open": "763.5",
                        "bid": "763.4",
                        "ask": "951.3",
                        "low": "763.5",
                        "high": "763.5",
                        "volume": "0.000000000000000000",
                        "change": "0.0",
                        "changePercent": "0"
                    },
                    "imx_krw": {
                        "timestamp": 1691312457456,
                        "last": "996.3",
                        "open": "976.7",
                        "bid": "987.5",
                        "ask": "992.5",
                        "low": "991",
                        "high": "996.3",
                        "volume": "90.684543204201545719",
                        "change": "19.6",
                        "changePercent": "2.01"
                    },
                    "waxl_krw": {
                        "timestamp": 1691257215921,
                        "last": "622.5",
                        "open": "622.5",
                        "bid": "471.6",
                        "ask": "620",
                        "low": "622.5",
                        "high": "622.5",
                        "volume": "0.000000",
                        "change": "0.0",
                        "changePercent": "0"
                    },
                    "cfg_krw": {
                        "timestamp": 1690765520609,
                        "last": "320.3",
                        "open": "320.3",
                        "bid": "321.3",
                        "ask": "374.8",
                        "low": "320.3",
                        "high": "320.3",
                        "volume": "0.000000000000000000",
                        "change": "0.0",
                        "changePercent": "0"
                    },
                    "storj_krw": {
                        "timestamp": 1691380848527,
                        "last": "386.1",
                        "open": "384.4",
                        "bid": "385.4",
                        "ask": "389.3",
                        "low": "386.1",
                        "high": "386.1",
                        "volume": "30.00000000",
                        "change": "1.7",
                        "changePercent": "0.44"
                    },
                    "pokt_krw": {
                        "timestamp": 1691231546601,
                        "last": "39.24",
                        "open": "39.24",
                        "bid": "34.43",
                        "ask": "39.24",
                        "low": "39.24",
                        "high": "39.24",
                        "volume": "0.000000",
                        "change": "0.00",
                        "changePercent": "0"
                    },
                    "ens_krw": {
                        "timestamp": 1691355321125,
                        "last": "11920",
                        "open": "11970",
                        "bid": "11990",
                        "ask": "12000",
                        "low": "11890",
                        "high": "12000",
                        "volume": "42.092950418410040000",
                        "change": "-50",
                        "changePercent": "-0.42"
                    },
                    "op_krw": {
                        "timestamp": 1691382390535,
                        "last": "2314",
                        "open": "2247",
                        "bid": "2304",
                        "ask": "2309",
                        "low": "2240",
                        "high": "2314",
                        "volume": "2144.863799217641713544",
                        "change": "67",
                        "changePercent": "2.98"
                    },
                    "rune_krw": {
                        "timestamp": 1691323597849,
                        "last": "1242",
                        "open": "1225",
                        "bid": "1242",
                        "ask": "1244",
                        "low": "1242",
                        "high": "1242",
                        "volume": "50.00000000",
                        "change": "17",
                        "changePercent": "1.39"
                    },
                    "sushi_krw": {
                        "timestamp": 1691375297340,
                        "last": "923.5",
                        "open": "904.8",
                        "bid": "920.7",
                        "ask": "923.3",
                        "low": "910.2",
                        "high": "923.5",
                        "volume": "1310.490246665528935565",
                        "change": "18.7",
                        "changePercent": "2.07"
                    },
                    "ocean_krw": {
                        "timestamp": 1691325170947,
                        "last": "455.6",
                        "open": "446.5",
                        "bid": "454.5",
                        "ask": "458.8",
                        "low": "453.6",
                        "high": "455.6",
                        "volume": "500.000000000000000000",
                        "change": "9.1",
                        "changePercent": "2.04"
                    },
                    "celo_krw": {
                        "timestamp": 1691378825134,
                        "last": "656",
                        "open": "657.5",
                        "bid": "659.9",
                        "ask": "664.4",
                        "low": "651.3",
                        "high": "675.9",
                        "volume": "4546.713547910000000000",
                        "change": "-1.5",
                        "changePercent": "-0.23"
                    },
                    "etc_krw": {
                        "timestamp": 1691380814219,
                        "last": "23740",
                        "open": "23850",
                        "bid": "23690",
                        "ask": "23730",
                        "low": "23600",
                        "high": "23830",
                        "volume": "542.09947464",
                        "change": "-110",
                        "changePercent": "-0.46"
                    },
                    "trx_krw": {
                        "timestamp": 1691377522959,
                        "last": "101.8",
                        "open": "102.8",
                        "bid": "101.9",
                        "ask": "102",
                        "low": "101.8",
                        "high": "102.8",
                        "volume": "175423.597085",
                        "change": "-1.0",
                        "changePercent": "-0.97"
                    },
                    "ilv_krw": {
                        "timestamp": 1691359910689,
                        "last": "56350",
                        "open": "56100",
                        "bid": "56550",
                        "ask": "69050",
                        "low": "56150",
                        "high": "56350",
                        "volume": "2.881834210000000000",
                        "change": "250",
                        "changePercent": "0.45"
                    },
                    "rari_krw": {
                        "timestamp": 1691336453056,
                        "last": "1488",
                        "open": "1402",
                        "bid": "1390",
                        "ask": "1450",
                        "low": "1402",
                        "high": "1488",
                        "volume": "95.671413515053763441",
                        "change": "86",
                        "changePercent": "6.13"
                    },
                    "apt_krw": {
                        "timestamp": 1691377961344,
                        "last": "8920",
                        "open": "8980",
                        "bid": "8890",
                        "ask": "8915",
                        "low": "8900",
                        "high": "9005",
                        "volume": "56.30120030",
                        "change": "-60",
                        "changePercent": "-0.67"
                    },
                    "one_krw": {
                        "timestamp": 1691371867402,
                        "last": "15.46",
                        "open": "14.82",
                        "bid": "15.4",
                        "ask": "15.55",
                        "low": "15.41",
                        "high": "15.46",
                        "volume": "2965.918122020000000000",
                        "change": "0.64",
                        "changePercent": "4.32"
                    },
                    "fil_krw": {
                        "timestamp": 1691377521021,
                        "last": "5560",
                        "open": "5455",
                        "bid": "5540",
                        "ask": "5550",
                        "low": "5465",
                        "high": "5565",
                        "volume": "4970.753118968888888889",
                        "change": "105",
                        "changePercent": "1.92"
                    },
                    "xec_krw": {
                        "timestamp": 1691382965884,
                        "last": "0.0392",
                        "open": "0.0386",
                        "bid": "0.039",
                        "ask": "0.0392",
                        "low": "0.0383",
                        "high": "0.0392",
                        "volume": "161313431.89",
                        "change": "0.0006",
                        "changePercent": "1.55"
                    },
                    "bal_krw": {
                        "timestamp": 1691371626046,
                        "last": "5750",
                        "open": "5680",
                        "bid": "5740",
                        "ask": "5745",
                        "low": "5695",
                        "high": "5750",
                        "volume": "100.382569542188930000",
                        "change": "70",
                        "changePercent": "1.23"
                    },
                    "nu_krw": {
                        "timestamp": 1691382230298,
                        "last": "95.12",
                        "open": "95.12",
                        "bid": "95.12",
                        "ask": "103.9",
                        "low": "95.12",
                        "high": "95.13",
                        "volume": "18017.148595690000000000",
                        "change": "0.00",
                        "changePercent": "0"
                    }
                }
            """.trimIndent()

        // TypeToken을 사용하여 Map<String, MarketData> 타입을 얻습니다.
        val typeToken = object : TypeToken<Map<String, MarketData>>() {}.type

        // JSON 데이터를 파싱하여 MarketData 객체들의 맵으로 변환합니다.
        val marketDataMap: Map<String, MarketData> = Gson().fromJson(jsonData, typeToken)

        // 이제 currency_pair와 해당 MarketData 객체들의 맵을 얻었습니다.
        // 필요한 경우 이를 리스트로 변환할 수 있습니다.
        val marketDataList = marketDataMap.entries.map { (currencyPair, data) ->
            data.copy(currencyPair = currencyPair)
        }

        // view용 마켓데이터 전처리
        marketDataPreprocessedDataList = marketDataList.map { marketData ->
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
        // 리스트 조회 결과는 거래대금이 높은 순서대로 정렬하여 출력
        marketDataPreprocessedDataList =
            marketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.volume.toDouble() }

        // 즐겨찾기 리스트
        favoriteMarketDataPreprocessedDataList = marketDataPreprocessedDataList.filter { data ->
            data.showMarketData.currencyPair in favorites
        }

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
        favoriteMarketDataPreprocessedDataList = tempFavoriteMarketDataPreprocessedDataList.filter { data ->
            data.showMarketData.currencyPair.contains(searchText, ignoreCase = true)
        }
    }

    fun sortList(sortButtonNum: Int) {
        searchText = ""
        when (sortButtonNum) {
            // 정렬 버튼 - 가상자산명 내림차순
            11 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.currencyPair }
            }
            // 정렬 버튼 - 가상자산명 오름차순
            12 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.currencyPair }
            }
            // 정렬 버튼 - 현재가 내림차순
            21 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.last.toDouble() }
            }
            // 정렬 버튼 - 현재가 오름차순
            22 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.last.toDouble() }
            }
            // 정렬 버튼 - 24시간 내림차순
            31 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.changePercent.toDouble() }
            }
            // 정렬 버튼 - 24시간 오름차순
            32 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.changePercent.toDouble() }
            }
            // 정렬 버튼 - 거래대금 내림차순
            41 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedByDescending { it.originalMarketData.volume.toDouble() }
            }
            // 정렬 버튼 - 거래대금 오름차순
            42 -> {
                marketDataPreprocessedDataList =
                    tempMarketDataPreprocessedDataList.sortedBy { it.originalMarketData.volume.toDouble() }
            }

            else -> {}
        }
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