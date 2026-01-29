package com.example.loudquestion.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.loudquestion.screens.MainScreen
import com.example.loudquestion.screens.SettingScreen
import com.example.loudquestion.viewmodel.MainScreenViewModel

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
) {
    val navHostState = NavigationState(navHostController = navHostController)
    val viewModel: MainScreenViewModel = viewModel()
    
    NavHost(navController = navHostController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {navBackStackEntry ->
            MainScreen(viewModel = viewModel, onSettingButtonPressed = { player ->
                navHostState.navigateTo(
                    Screen.Settings.getRouteWithArgs(player = player)
                )
            })
        }
        
        composable(Screen.Settings.route) {navBackStackEntry ->
            val playerName = navBackStackEntry.arguments?.getString(Screen.KEY_PLAYER_NAME) ?: "Error"
            
            SettingScreen(
                playerName = playerName, onBackPressedButton = { navHostController.popBackStack() })
        }
    }
}