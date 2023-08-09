package com.uyjang.korbittest.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import kotlin.math.roundToInt


@Preview(showBackground = true)
@Composable
fun PreviewFloatingButton() {
    FloatingButton {

    }
}

@Composable
fun FloatingButton(
    onClick: () -> Unit
) {
    val density = LocalDensity.current
    var offsetX by remember { mutableStateOf(with(density) { -30.dp.toPx() }) }
    var offsetY by remember { mutableStateOf(with(density) { -80.dp.toPx() }) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp

    Popup(
        alignment = Alignment.BottomEnd,
        offset = IntOffset(x = offsetX.roundToInt(), y = offsetY.roundToInt())
    ) {
        Box(modifier = Modifier
            .padding(5.dp)
            .shadow(3.dp, CircleShape)
            .clip(CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                    // 화면 밖으로 나가는거 방지
                    if (offsetX > 0) {
                        offsetX = 0F
                    }
                    if (offsetY > 0) {
                        offsetY = 0F
                    }
                    if (-offsetX.toDp() > screenWidth.dp) {
                        offsetX = -screenWidth.dp.toPx()
                    }
                    if (-offsetY.toDp() > screenHeight.dp) {
                        offsetY = -screenHeight.dp.toPx()
                    }
                }
            }
            .defaultMinSize(60.dp, 60.dp)
        ) {
            Column(
                modifier = Modifier
                    .size(60.dp, 60.dp)
                    .background(Color(0xFF43A2F7))
                    .clickable { onClick() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}