package com.example.loudquestion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question
import com.example.loudquestion.viewmodel.LoudQuestionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPlayerUI(
    viewModel: LoudQuestionViewModel,
    gameIsStarted: Boolean,
    unresolvedAnswerList: List<Question>,
    resolvedAnswerList: List<Question>,
    drawerState: DrawerState,
    playerList: List<Player>
) {
    val state by viewModel.gameVM.collectAsState()
    val scope = rememberCoroutineScope()
    val isDialogOpen = state.activePlayer != null
    val resolvedAnswerListUI = state.resolvedQuestion
    val unresolvedAnswerListUI = state.unresolvedQuestion
    
    var showSuccessfulQuestion by remember { mutableStateOf(false) }
    var showFailedQuestion by remember { mutableStateOf(false) }
    
    ShowCompletedQuestion(
        questionList = resolvedAnswerListUI,
        isShowed = showSuccessfulQuestion,
        onDismiss = { showSuccessfulQuestion = false })
    
    ShowCompletedQuestion(
        questionList = unresolvedAnswerListUI,
        isShowed = showFailedQuestion,
        onDismiss = { showFailedQuestion = false })
    
    DisplayPlayerInfo(viewModel = viewModel, isDialogShowed = isDialogOpen)
    
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceAround
            ) {
                Text(
                    modifier = Modifier.weight(0.4f),
                    text = when {
                        gameIsStarted -> "Играем"
                        !gameIsStarted -> "Подготовка"
                        
                        else -> ""
                    }
                )
               
                    IconButton(modifier = Modifier.weight(0.3f),onClick = {
                        if(unresolvedAnswerListUI.isNotEmpty()){
                            showFailedQuestion = true
                        }
                    }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Close, null)
                            Text(text = "${unresolvedAnswerList.size}")
                        }
                    }
                
                
                    IconButton(
                        modifier = Modifier.weight(0.3f), onClick = {
                        if(resolvedAnswerListUI.isNotEmpty()){
                            showSuccessfulQuestion = true
                        }
                    }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Done, null)
                            Text(text = "${resolvedAnswerList.size}")
                        }
                    }
            }
        }, navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply { open() }
                }
            }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        })
    }) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues), columns = GridCells.Fixed(2)
        ) {
            items(playerList, key = { player -> player.playerId }) { player ->
                PlayerDisplay(player, onClick = { viewModel.setActivePlayer(player = player) })
            }
        }
    }
}
