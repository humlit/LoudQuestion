package com.example.loudquestion.classes

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.util.UUID

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Player(
    val playerId: String = getRandomUUID(),
    val playerName: String,
    val playerQuestion: List<Question> = emptyList(),
    val playerActiveRoundQuestion: Question? = null,
)
