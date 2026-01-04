package com.kth.stepapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kth.stepapp.ui.screens.DemoScreen
import com.kth.stepapp.ui.screens.HomeScreen
import com.kth.stepapp.ui.screens.LoginScreen
import com.kth.stepapp.ui.viewmodels.DemoVM
import com.kth.stepapp.ui.viewmodels.LoginVM
import com.kth.stepapp.ui.screens.CalendarScreen
import com.kth.stepapp.ui.viewmodels.CalendarVM
import com.kth.stepapp.ui.screens.ProfileScreen
import com.kth.stepapp.ui.viewmodels.ProfileVM



@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        composable(Routes.HOME) {
            HomeScreen(
                onGoToDemo = {
                    navController.navigate(Routes.DEMO)
                },
                onGoToLogin = {
                    navController.navigate(Routes.LOGIN)
                },
                onGoToCalendar = {
                    navController.navigate(Routes.CALENDAR)
                },
                onGoToProfile = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }


        composable(Routes.LOGIN) {
            val loginVM: LoginVM = viewModel(factory = LoginVM.Factory)

            LoginScreen(
                vm = loginVM,
                onSuccess = {
                    navController.popBackStack(Routes.HOME, false)
                },
                onBack = {
                    navController.popBackStack()
                }
            )

        }

        composable(Routes.DEMO) {
            val demoViewModel: DemoVM = viewModel(factory = DemoVM.Factory)

            DemoScreen(
                vm = demoViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.CALENDAR) {
            val calendarVM: CalendarVM = viewModel(factory = CalendarVM.Factory)

            CalendarScreen(
                vm = calendarVM,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.PROFILE) {
            val profileVM: ProfileVM = viewModel(factory = ProfileVM.Factory)

            ProfileScreen(
                vm = profileVM,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}

