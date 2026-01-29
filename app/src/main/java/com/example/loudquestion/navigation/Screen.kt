package com.example.loudquestion.navigation

import android.net.Uri
import com.example.loudquestion.classes.Player
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

sealed class Screen(val route: String) { object Main : Screen(route = ROUTE_MAIN)
    
    object Settings : Screen(route = ROUTE_SETTINGS) {
        
        private const val ROUTE_FOR_ARGS = "settings"
        
        fun getRouteWithArgs(players: List<Player>): String {
            return "$ROUTE_FOR_ARGS/${Json.encodeToString(players).encode()}"
        }
    }
    
    companion object {
        const val KEY_PLAYER_INFO = "players_info"
        
        const val ROUTE_MAIN = "main"
        const val ROUTE_SETTINGS = "settings/{$KEY_PLAYER_INFO}"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}