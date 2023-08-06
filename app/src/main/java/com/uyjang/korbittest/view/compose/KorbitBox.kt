package com.uyjang.korbittest.view.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme

@Preview(showBackground = true)
@Composable
fun PreviewKorbitBox() {
    KorbitTestTheme {
        KorbitBox() {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Text")
                Text(text = "Text")
            }
        }
    }
}

@Composable
fun KorbitBox(modifier: Modifier = Modifier, content: @Composable (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .then(modifier)
            .defaultMinSize(10.dp, 10.dp)
            .border(1.dp, LocalContentColor.current, shape = RoundedCornerShape(5.dp))
    ) {
        content?.invoke()
    }
}