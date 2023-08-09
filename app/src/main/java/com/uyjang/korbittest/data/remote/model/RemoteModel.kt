package com.uyjang.korbittest.data.remote.model

data class TickerDetail(
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
