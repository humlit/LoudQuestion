package com.example.loudquestion.classes

import java.util.UUID

data class Player(
    val playerId: UUID = UUID.randomUUID(),
    val playerName: String,
    val playerQuestion: List<Question> = emptyList()
)
