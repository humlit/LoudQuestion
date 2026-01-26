package com.example.loudquestion.viewmodel

import android.app.Application
import android.content.Context
import android.text.style.BackgroundColorSpan
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loudquestion.classes.Game
import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val LOUD_QUESTIONS_STORE_NAME = "load_question"

val GAME_DATA_KEY = stringPreferencesKey("load_game_data")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LOUD_QUESTIONS_STORE_NAME)

class LoudQuestionViewModel(application: Application, questions: List<Question>) : AndroidViewModel(application) {
    
    val context = getApplication<Application>()
    var gameData: Game
    
    val initialState = Game(
        activePlayer = null,
        playerList = emptyList(),
        isGameStart = false,
        timerTime = 90,
        resolvedQuestion = emptyList(),
        unresolvedQuestion = emptyList()
    )
    
    init {
        runBlocking {
            val startGameStore = Json.encodeToString<Game>(initialState)
            
            gameData = Json.decodeFromString<Game>(context.dataStore.data.first()[GAME_DATA_KEY] ?: startGameStore)
        }
    }
    
    private var _gameVM = MutableStateFlow<Game>(gameData)
    val gameVM = _gameVM.asStateFlow()
    
    suspend fun setContext() {
        context.dataStore.edit { preferences ->
            val newGameDataJson = Json.encodeToString<Game>(gameVM.value)
            
            preferences[GAME_DATA_KEY] = newGameDataJson
        }
    }
    
    init {
        distributionOfQuestionWatcher()
    }
    
    private fun distributionOfQuestionWatcher() {
        viewModelScope.launch(
            context = Dispatchers.Default + CoroutineName("BackgroundQuestionWatcher")
        ) {
            while (true) {
                val currentState = _gameVM.value
                
                val allPlayersHasNotActiveQuestion = currentState.playerList.all {
                    it.playerActiveRoundQuestion == null
                }
                
                if (currentState.isGameStart && allPlayersHasNotActiveQuestion) {
                    distributionOfQuestion()
                }
                
                delay(200)
            }
        }
    }
    
    private fun distributionOfQuestion() { //TODO: NOT WORKING. NEED CORRECTING
        _gameVM.update { currentState ->
            
            val players = currentState.playerList.toMutableList()
            
            val updatedPlayers = players.map { targetPlayer ->
                
                val donors = players.filter {
                    it.playerId != targetPlayer.playerId && it.playerQuestion.isNotEmpty()
                }
                
                if (donors.isEmpty()) return@map targetPlayer
                
                val donor = donors.random()
                val question = donor.playerQuestion.random()
                
                // обновляем донора
                val donorIndex = players.indexOfFirst { it.playerId == donor.playerId }
                players[donorIndex] = donor.copy(
                    playerQuestion = donor.playerQuestion - question
                )
                
                targetPlayer.copy(
                    playerActiveRoundQuestion = question
                )
            }
            
            currentState.copy(playerList = updatedPlayers)
        }
    }
    
    fun changeGameStatus(gameStatus: Boolean) {
        _gameVM.update { currentState ->
            
            currentState.copy(
                isGameStart = gameStatus
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
            val newQuestion = Question(question = question, owner = activePlayer)
            
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
                if (player.playerQuestion.any { it.questionId == question.questionId }) {
                    player.copy(
                        playerQuestion = player.playerQuestion - question
                    )
                } else {
                    player
                }
                
                if (player.playerId == currentState.activePlayer?.playerId) {
                    player.copy(
                        playerActiveRoundQuestion = null
                    )
                } else {
                    player
                }
            }
            
            val activePlayer = currentState.activePlayer?.copy(
                playerActiveRoundQuestion = null
            )
            
            currentState.copy(
                activePlayer = activePlayer, playerList = newPlayerList
            )
        }
    }
    
    fun addQuestionToResolveList(question: Question) {
        _gameVM.update { currentState ->
            val resolveQuestionList = currentState.resolvedQuestion.toMutableList().apply {
                add(question)
            }
            
            currentState.copy(
                resolvedQuestion = resolveQuestionList
            )
        }
    }
    
    fun resetGameState() {
        _gameVM.update { initialState }
    }
    
    fun clearCompletedQuestion() {
        _gameVM.update { currentState ->
            currentState.copy(
                resolvedQuestion = emptyList(), unresolvedQuestion = emptyList()
            )
        }
    }
    
    fun addQuestionToUnresolveList(question: Question) {
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
                activePlayer = null, playerList = newPlayerList
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