package com.example.loudquestion.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.loudquestion.classes.Player
import com.example.loudquestion.screens.MainScreen
import com.example.loudquestion.screens.SettingScreen
import com.example.loudquestion.viewmodel.MainScreenViewModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
) {
    val navHostState = NavigationState(navHostController = navHostController)
    val viewModel: MainScreenViewModel = viewModel()
    
    NavHost(navController = navHostController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {navBackStackEntry ->
            MainScreen(viewModel = viewModel, onSettingButtonPressed = { players ->
                navHostState.navigateTo(
                    Screen.Settings.getRouteWithArgs(players = players)
                )
            })
        }
        
        composable(
            route = Screen.Settings.route,
            arguments = listOf(
                navArgument(name = Screen.KEY_PLAYER_INFO){
                    type = NavType.StringType
                }
            )
        ) {navBackStackEntry ->
            val playerInfoJson = navBackStackEntry.arguments?.getString(Screen.KEY_PLAYER_INFO) ?: "Error"
            val playerInfo = Json.decodeFromString<List<Player>>(playerInfoJson)
            
            SettingScreen(
                playersList = playerInfo, onBackPressedButton = { navHostController.popBackStack() })
        }
    }
}