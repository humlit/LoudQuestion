package com.example.loudquestion.classes

import java.util.UUID

data class Question(
    val questId: UUID = UUID.randomUUID(),
    val question: String,
)