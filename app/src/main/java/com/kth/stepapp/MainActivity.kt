package com.kth.stepapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kth.stepapp.ui.theme.StepAppTheme
import com.kth.stepapp.ui.navigation.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StepAppTheme {
                AppNavGraph()
            }
        }
    }
}
