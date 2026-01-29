package com.example.loudquestion.classes

import android.annotation.SuppressLint
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Player(
    val playerId: String = UUID.randomUUID().toString(),
    val playerName: String,
    val playerImage: Int,
    val playerQuestion: List<Question> = emptyList()
) {
    
    companion object {
        
        val NavigationType: NavType<Player> = object : NavType<Player>(false) {
            override fun get(
                bundle: SavedState,
                key: String
            ): Player {
                val json = bundle.getString(key) ?: error("No player found")
                return Json.decodeFromString(json)
            }
            
            override fun parseValue(value: String): Player {
                return Json.decodeFromString<Player>(value)
            }
            
            override fun put(
                bundle: SavedState,
                key: String,
                value: Player
            ) {
                val valueJson = Json.encodeToString<Player>(value)
                bundle.putString(key, valueJson)
            }
        }
    }
}
