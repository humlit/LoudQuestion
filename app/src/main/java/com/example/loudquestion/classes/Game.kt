package com.example.loudquestion.classes

data class Game(
    val activePlayer: Player?,
    val playerList: List<Player>,
    val isGameStart: Boolean,
    val timerTime: Int,
    val resolvedQuestion: List<Question>,
    val unresolvedQuestion: List<Question>,
)
