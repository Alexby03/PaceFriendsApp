package com.kth.stepapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kth.stepapp.ui.screens.DemoScreen
import com.kth.stepapp.ui.screens.HomeScreen
import com.kth.stepapp.ui.viewmodels.DemoVM

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onGoToDemo = {
                    navController.navigate("demo")
                }
            )
        }

        composable("demo") {
            val demoViewModel: DemoVM = viewModel(factory = DemoVM.Factory)
            DemoScreen(
                vm = demoViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
