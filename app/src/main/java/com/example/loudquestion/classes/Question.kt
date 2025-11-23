package com.example.loudquestion.classes

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import java.util.UUID

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Question(
    val questId: String = UUID.randomUUID().toString(),
    val question: String,
)