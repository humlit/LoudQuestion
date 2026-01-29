package com.example.loudquestion.components.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.loudquestion.classes.Player
import com.example.loudquestion.components.commoncomponents.StyledText
import com.example.loudquestion.ui.theme.DarkIndigo
import com.example.loudquestion.ui.theme.IndigoGradient
import com.example.loudquestion.viewmodel.MainScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPlayerUI(
    viewModel: MainScreenViewModel,
    gameIsStarted: Boolean,
    drawerState: DrawerState,
    playerList: List<Player>
) {
    val state by viewModel.gameVM.collectAsState()
    val scope = rememberCoroutineScope()
    val isDialogOpen = state.activePlayer != null
    val resolvedAnswerList = state.resolvedQuestion
    val unresolvedAnswerList = state.unresolvedQuestion
    
    var isCompletedQuestionShowed by remember { mutableStateOf(false) }
    var isTimerEndWarningShowed by remember { mutableStateOf(false) }
    
    ShowCompletedQuestion(
        failedQuestionList = unresolvedAnswerList,
        successQuestionList = resolvedAnswerList,
        isShowed = isCompletedQuestionShowed,
        onDismiss = { isCompletedQuestionShowed = false })
    
    TimeEndWarningDialog(isShowed = isTimerEndWarningShowed, onDismiss = {
        isTimerEndWarningShowed = false
    })
    
    DisplayPlayerInfo(viewModel = viewModel, isDialogShowed = isDialogOpen, onFinishTimer = {
        isTimerEndWarningShowed = true
    })
    
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceAround
                ) {
                    StyledText(
                        modifier = Modifier.weight(0.4f), text = when {
                            gameIsStarted -> "Играем"
                            !gameIsStarted -> "Подготовка"
                            
                            else -> ""
                        }, style = MaterialTheme.typography.labelLarge
                    )
                    
                    Row(
                        modifier = Modifier.clickable(onClick = {
                            isCompletedQuestionShowed = true
                        })
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Close, null, tint = Color.White)
                            StyledText(text = "${unresolvedAnswerList.size}")
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Done, null, tint = Color.White)
                            StyledText(text = "${resolvedAnswerList.size}")
                        }
                    }
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkIndigo
            ), navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        drawerState.apply { open() }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null, tint = Color.White)
                }
            })
    }) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = IndigoGradient)
                )
                .padding(paddingValues), columns = GridCells.Fixed(2)
        ) {
            items(playerList, key = { player -> player.playerId }) { player ->
                PlayerDisplay(player, onClick = { viewModel.setActivePlayer(player = player) })
            }
        }
    }
}
