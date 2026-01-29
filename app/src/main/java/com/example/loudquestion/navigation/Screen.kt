package com.example.loudquestion.navigation

import com.example.loudquestion.classes.Player

sealed class Screen(val route: String) {
    object Main: Screen(route = ROUTE_MAIN)
    
    object Settings: Screen(route = ROUTE_SETTINGS) {
        
        private const val ROUTE_FOR_ARGS = "settings"
        
        fun getRouteWithArgs(player: Player): String {
            return "$ROUTE_FOR_ARGS/${player.playerName}"
        }
    }
    
    companion object {
        
        const val KEY_PLAYER_NAME = "player_name"
        
        const val ROUTE_MAIN = "main"
        const val ROUTE_SETTINGS = "settings/{$KEY_PLAYER_NAME}"
    }
}