package com.uyjang.korbittest.base

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.uyjang.korbittest.databinding.ActivityMainBinding
import com.uyjang.korbittest.view.ui.theme.KorbitTestTheme

class MainActivity : AppCompatActivity() {

    // 뷰모델
    val viewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                KorbitTestTheme {
                    // TODO : uiState 팝업 넣기.
                }
            }
        }

        setContentView(binding.root)
    }
}