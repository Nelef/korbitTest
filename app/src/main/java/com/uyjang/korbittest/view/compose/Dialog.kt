package com.uyjang.korbittest.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.uyjang.korbittest.view.ui.theme.LightBlue

@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = LightBlue,
            modifier = Modifier.size(48.dp)
        )
    }
    dismissOnClickOutside()
}

@Composable
fun AlertDialog(
    contentText: String,
    properties: PopupProperties = PopupProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    ),
    onDismissRequest: () -> Unit
) {
    Popup(
        popupPositionProvider = WindowCenterOffsetPositionProvider(),
        properties = properties
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .size(250.dp, 150.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 24.dp,
                            top = 36.dp,
                            end = 24.dp,
                            bottom = 36.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = contentText, textAlign = TextAlign.Center, fontSize = 14.sp)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable { onDismissRequest() }
                        .background(
                            LightBlue
                        ),
                    contentAlignment = Alignment.Center
                ) { Text(text = "확인", color = Color.White, fontSize = 14.sp) }
            }
        }
    }
    dismissOnClickOutside()
}

@Composable
fun dismissOnClickOutside() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
    ) { }
}

class WindowCenterOffsetPositionProvider(
    private val x: Int = 0,
    private val y: Int = 0
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(
            (windowSize.width - popupContentSize.width) / 2 + x,
            (windowSize.height - popupContentSize.height) / 2 + y
        )
    }
}