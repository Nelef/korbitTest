package com.uyjang.korbittest.view.fragment

import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import com.uyjang.korbittest.base.BaseFragment
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme
import com.uyjang.korbittest.viewModel.MainViewModel

class MainFragment : BaseFragment() {

    companion object {
        private const val WEB_INTERFACE_NAME = "WebInterface"
    }

    private val viewModel: MainViewModel by viewModels()

    init {
        baseCompose.content = {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

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
fun GreetingPreview() {
    KorbitTestTheme {
        HelloWorld("Android") {}
    }
}