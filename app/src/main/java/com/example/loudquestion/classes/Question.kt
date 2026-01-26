package com.example.loudquestion.classes

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.util.UUID

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Question(
    val questionId: String = getRandomUUID(),
    val question: String,
    val owner: Player,
)