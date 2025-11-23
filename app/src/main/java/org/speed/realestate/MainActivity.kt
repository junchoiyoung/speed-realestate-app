package org.speed.realestate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.speed.realestate.constant.AppDestinations
import org.speed.realestate.screens.main.RealEstateMainScreen
import org.speed.realestate.screens.result.RealEstateResultScreen
import org.speed.realestate.ui.RealEstateViewModelProvider
import org.speed.realestate.ui.viewmodel.RealEstateViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                val viewModel: RealEstateViewModel = viewModel(
                    factory = RealEstateViewModelProvider.Factory
                )

                NavHost(
                    navController = navController,
                    startDestination = AppDestinations.MAIN_SCREEN
                ) {
                    composable(route = AppDestinations.MAIN_SCREEN) {
                        RealEstateMainScreen(
                            viewModel = viewModel,
                            onSearchBarClick = {
                                navController.navigate(AppDestinations.RESULT_SCREEN)
                            }
                        )
                    }

                    composable(route = AppDestinations.RESULT_SCREEN) {
                        RealEstateResultScreen(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.popBackStack()
                            })
                    }
                }
            }
        }
    }
}

