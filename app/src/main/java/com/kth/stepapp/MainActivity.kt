package com.kth.stepapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kth.stepapp.ui.theme.StepAppTheme
import com.kth.stepapp.ui.screens.DemoScreen
import com.kth.stepapp.ui.viewmodels.DemoVM
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val demoViewModel: DemoVM = viewModel(factory = DemoVM.Factory)
            StepAppTheme {
                DemoScreen(demoViewModel)
            }
        }
    }
}