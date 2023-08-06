package com.uyjang.korbittest.view.fragment

import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.uyjang.korbittest.base.BaseFragment
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme
import com.uyjang.korbittest.viewModel.MainViewModel

class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by viewModels()

    init {
        baseCompose.content = {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                TabScreen()
            }
        }
    }
}

@Composable
fun TabScreen() {
    var tabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("마켓", "즐겨찾기")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> HelloWorld(name = "test1") {
                
            }
            1 -> HelloWorld(name = "test2") {

            }
        }
    }
}

@Composable
fun HelloWorld(name: String, onClick : () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello $name!")
        Button(onClick = { onClick() }) {
            Text(text = "Go to FirstFragment")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainFragmentPreview() {
    KorbitTestTheme {
        TabScreen()
    }
}