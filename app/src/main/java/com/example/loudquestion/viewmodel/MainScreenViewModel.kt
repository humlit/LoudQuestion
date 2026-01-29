package com.example.loudquestion.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loudquestion.R
import com.example.loudquestion.classes.Game
import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val LOUD_QUESTIONS_STORE_NAME = "load_question"

val GAME_DATA_KEY = stringPreferencesKey("load_game_data")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = LOUD_QUESTIONS_STORE_NAME)

class MainScreenViewModel(application: Application) : AndroidViewModel(application) {
    
    val context = getApplication<Application>()
    
    private val json = Json {
        ignoreUnknownKeys = true
    }
    
    val initialState = Game(
        activePlayer = null,
        playerList = emptyList(),
        isGameStart = false,
        timerTime = 90,
        resolvedQuestion = emptyList(),
        unresolvedQuestion = emptyList()
    )
    
    var gameData: Game = initialState
        private set
    
    private var _gameVM = MutableStateFlow<Game>(gameData)
    val gameVM = _gameVM.asStateFlow()
    
    init {
        viewModelScope.launch {
            val storedGameJson = context.dataStore.data.first()[GAME_DATA_KEY]
            
            val game = try {
                storedGameJson?.let {
                    json.decodeFromString<Game>(it)
                } ?: initialState
            } catch (e: Exception) {
                initialState
            }
            
            _gameVM.value = game
        }
    }
    
    suspend fun setContext() {
        context.dataStore.edit { preferences ->
            val newGameDataJson = Json.encodeToString<Game>(gameVM.value)
            
            preferences[GAME_DATA_KEY] = newGameDataJson
        }
    }
    
    fun changeGameStatus(gameStatus: Boolean) {
        _gameVM.update { currentState ->
            
            currentState.copy(
                isGameStart = gameStatus
            )
        }
    }
    
    fun addPlayer(
        playerName: String,
        playerImage: Int
    ) {
        _gameVM.update { currentState ->
            val newPlayer = Player(playerName = playerName, playerImage = playerImage)
            
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
    
    fun resetGameState() {
        _gameVM.update { initialState }
    }
    
    fun editingQuestionText(question: Question) {
        _gameVM.update { currentState ->
            val activePlayerId = currentState.activePlayer?.playerId
            
            val newPlayerList = currentState.playerList.map { player ->
                if (player.playerId == activePlayerId) {
                    player.copy(
                        playerQuestion = player.playerQuestion.map { plQuestion ->
                            if (plQuestion.questId == question.questId) {
                                plQuestion.copy(
                                    question = question.question
                                )
                            } else plQuestion
                        })
                } else player
            }
            
            val newActivePlayer = newPlayerList.find { it.playerId == activePlayerId }
            
            currentState.copy(
                activePlayer = newActivePlayer, playerList = newPlayerList
            )
        }
    }
    
    fun changeReadOnlyStatusOnQuestion(question: Question) {
        _gameVM.update { currentState ->
            val activePlayerId = currentState.activePlayer?.playerId ?: return@update currentState
            
            val newPlayerList = currentState.playerList.map { player ->
                if (player.playerId != activePlayerId) return@map player
                
                player.copy(
                    playerQuestion = player.playerQuestion.map { plQuestion ->
                        if (plQuestion.questId == question.questId) {
                            plQuestion.copy(
                                isReadOnly = !plQuestion.isReadOnly
                            )
                        } else plQuestion.copy(
                            isReadOnly = true
                        )
                    })
            }
            
            val newActivePlayer = newPlayerList.find { it.playerId == activePlayerId }
            
            currentState.copy(
                activePlayer = newActivePlayer, playerList = newPlayerList
            )
        }
    }
    
    fun clearCompletedQuestion() {
        _gameVM.update { currentState ->
            currentState.copy(
                resolvedQuestion = emptyList(), unresolvedQuestion = emptyList()
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