package com.kth.stepapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kth.stepapp.PaceFriendsApplication
import com.kth.stepapp.ui.screens.ActivityScreen
import com.kth.stepapp.ui.screens.HomeScreen
import com.kth.stepapp.ui.screens.LoginScreen
import com.kth.stepapp.ui.viewmodels.ActivityVM
import com.kth.stepapp.ui.viewmodels.LoginVM
import com.kth.stepapp.ui.screens.CalendarScreen
import com.kth.stepapp.ui.viewmodels.CalendarVM
import com.kth.stepapp.ui.screens.ProfileScreen
import com.kth.stepapp.ui.screens.ResultScreen
import com.kth.stepapp.ui.viewmodels.HomeVM
import com.kth.stepapp.ui.viewmodels.ProfileVM
import com.kth.stepapp.ui.viewmodels.ResultVM


@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.HOME) {
            val homeVM: HomeVM = viewModel(factory = HomeVM.Factory)

            HomeScreen(
                vm = homeVM,
                onGoToActivity = { type ->
                    navController.navigate("activity/$type")
                },
                onGoToCalendar = {
                    navController.navigate(Routes.CALENDAR)
                },
                onGoToProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            val loginVM: LoginVM = viewModel(factory = LoginVM.Factory)

            LoginScreen(
                vm = loginVM,
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )

        }

        composable(
            route = Routes.ACTIVITY,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->

            val activityType = backStackEntry.arguments?.getString("type") ?: "Walking"

            val activityViewModel: ActivityVM = viewModel(
                factory = ActivityVM.provideFactory(
                    app = LocalContext.current.applicationContext as PaceFriendsApplication,
                    activityType = activityType
                )
            )

            ActivityScreen(
                vm = activityViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onResult = {
                    navController.navigate(Routes.RESULT)
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

        composable(Routes.RESULT) {
            val resultVM: ResultVM = viewModel(factory = ResultVM.Factory)

            ResultScreen(
                vm = resultVM
            )
        }
    }
}