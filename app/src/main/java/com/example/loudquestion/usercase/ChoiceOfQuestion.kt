package com.example.loudquestion.usercase

import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question

fun choiceOfQuestion(
    activePlayer: Player?,
    playerList: List<Player>
): Question? {
    val newPlayerList = playerList.toMutableList().filter { it.playerId != activePlayer?.playerId }
        .let { list ->
            val maxQuestionCount = list.maxOfOrNull { it.playerQuestion.size } ?: 0
            list.filter { it.playerQuestion.size == maxQuestionCount }
        }
    
    val question = newPlayerList.flatMap { it.playerQuestion }.randomOrNull()
    
    return question
}