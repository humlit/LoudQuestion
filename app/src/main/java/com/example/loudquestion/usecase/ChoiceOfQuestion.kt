package com.example.loudquestion.usecase

import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question

fun choiceOfQuestion(
    activePlayer: Player?,
    playerList: List<Player>
): Question? {
    val player = choiceOfPlayer(playerList = playerList, activePlayer = activePlayer)
    
    return player?.playerQuestion?.randomOrNull()
}

fun choiceOfPlayer(
    playerList: List<Player>,
    activePlayer: Player?
): Player? {
    val newPlayerList = playerList.toMutableList().filter { it.playerId != activePlayer?.playerId }
        .let { list ->
            val maxQuestionCount = list.maxOfOrNull { it.playerQuestion.size } ?: 0
            list.filter { it.playerQuestion.size == maxQuestionCount }
        }
    
    return newPlayerList.randomOrNull()
}
