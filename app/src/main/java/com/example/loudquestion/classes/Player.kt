package com.example.loudquestion.classes

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.util.UUID

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Player(
    val playerId: String = UUID.randomUUID().toString(),
    val playerName: String,
    val playerImage: Int,
    val playerQuestion: List<Question> = emptyList()
)
