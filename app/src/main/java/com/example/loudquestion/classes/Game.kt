package com.example.loudquestion.classes

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.util.UUID

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Game(
    val activePlayer: Player?,
    val playerList: List<Player>,
    val isGameStart: Boolean,
    val timerTime: Int,
    val resolvedQuestion: List<Question>,
    val unresolvedQuestion: List<Question>,
)

fun getRandomUUID(): String = UUID.randomUUID().toString()