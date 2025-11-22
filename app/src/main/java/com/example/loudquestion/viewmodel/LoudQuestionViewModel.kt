package com.example.loudquestion.viewmodel

import androidx.lifecycle.ViewModel
import com.example.loudquestion.classes.Game
import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoudQuestionViewModel : ViewModel() {
    
    val initialState = Game(
        activePlayer = null,
        playerList = emptyList(),
        isGameStart = false,
        timerTime = 90,
        resolvedQuestion = emptyList(),
        unresolvedQuestion = emptyList()
    )
    
    private var _gameVM = MutableStateFlow<Game>(initialState)
    val gameVM = _gameVM.asStateFlow()
    
    fun changeGameStatus() {
        _gameVM.update { currentState ->
            val currentGameStatus = currentState.isGameStart
            
            currentState.copy(
                isGameStart = !currentGameStatus
            )
        }
    }
    
    fun addPlayer(playerName: String) {
        _gameVM.update { currentState ->
            val newPlayer = Player(playerName = playerName)
            
            val playerList = currentState.playerList.toMutableList().apply {
                add(newPlayer)
            }
            
            currentState.copy(
                playerList = playerList
            )
        }
    }
    
    fun setActivePlayer(player: Player) {
        _gameVM.update { currentState ->
            currentState.copy(
                activePlayer = player
            )
        }
    }
    
    fun resetActivePlayer() {
        _gameVM.update { currentState ->
            currentState.copy(
                activePlayer = null
            )
        }
    }
    
    fun addSomeQuestion(
        question: String,
    ) {
        _gameVM.update { currentState ->
            val activePlayer = currentState.activePlayer ?: return@update currentState
            val newQuestion = Question(question = question)
            
            val newPlayerList = currentState.playerList.map { player ->
                if (activePlayer.playerId == player.playerId) {
                    player.copy(
                        playerQuestion = player.playerQuestion + newQuestion
                    )
                } else {
                    player
                }
            }
            
            val updatedActivePlayer = activePlayer.copy(
                playerQuestion = activePlayer.playerQuestion + newQuestion
            )
            
            currentState.copy(
                activePlayer = updatedActivePlayer, playerList = newPlayerList
            )
        }
    }
    
    fun removeQuestion(question: Question) {
        _gameVM.update { currentState ->
            val activePlayer = currentState.activePlayer ?: return@update currentState
            
            val updatedActivePlayer = activePlayer.copy(
                playerQuestion = activePlayer.playerQuestion - question
            )
            
            val newPlayerList = currentState.playerList.map { player ->
                if (activePlayer.playerId == player.playerId) {
                    player.copy(
                        playerQuestion = player.playerQuestion - question
                    )
                } else {
                    player
                }
            }
            
            currentState.copy(
                activePlayer = updatedActivePlayer, playerList = newPlayerList
            )
        }
    }
    
    fun deleteUsedQuestion(question: Question) {
        _gameVM.update { currentState ->
            val newPlayerList = currentState.playerList.map { player ->
                if (player.playerQuestion.any { it.questId == question.questId }) {
                    player.copy(
                        playerQuestion = player.playerQuestion - question
                    )
                } else {
                    player
                }
            }
            
            currentState.copy(
                playerList = newPlayerList
            )
        }
    }
    
    fun resolveQuestion(question: Question) {
        _gameVM.update { currentState ->
            val resolveQuestionList = currentState.resolvedQuestion.toMutableList().apply {
                add(question)
            }
            
            currentState.copy(
                resolvedQuestion = resolveQuestionList
            )
        }
    }
    
    fun unresolveQuestion(question: Question) {
        _gameVM.update { currentState ->
            val unresolveQuestionList = currentState.unresolvedQuestion.toMutableList().apply {
                add(question)
            }
            
            currentState.copy(
                unresolvedQuestion = unresolveQuestionList
            )
        }
    }
    
    fun deletePlayer() {
        _gameVM.update { currentState ->
            val activePlayer = currentState.activePlayer
            
            val newPlayerList = currentState.playerList.toMutableList().apply {
                removeAll { it.playerId == activePlayer?.playerId }
            }
            
            currentState.copy(
            activePlayer = null,
                playerList = newPlayerList
            )
        }
    }
    
    fun timerSetTimes(time: Int) {
        _gameVM.update { currentState ->
            currentState.copy(
                timerTime = time
            )
        }
    }
}