package com.example.loudquestion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question
import com.example.loudquestion.viewmodel.LoudQuestionViewModel

@Composable
fun DisplayPlayerInfo(
    viewModel: LoudQuestionViewModel,
    isDialogShowed: Boolean,
    onFinishTimer: () -> Unit
) {
    val state by viewModel.gameVM.collectAsState()
    val activePlayer = state.activePlayer
    val timerSetTime = state.timerTime
    
    val activePlayerActiveQuestion = activePlayer?.playerActiveRoundQuestion ?: Question(
        question = "Вопроса нет, чурка, не выёбуйся", owner = Player(playerName = "")
    )
    
    var questionText by remember { mutableStateOf("") }
    val playerQuest = activePlayer?.playerQuestion ?: emptyList()
    var questionIsShowed by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }
    
    if (!isDialogShowed) return
    
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(16.dp)
        ) {
            if (!state.isGameStart) {
                TextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black
                    ),
                    placeholder = { Text(text = "Какой вопрос?") },
                    leadingIcon = {
                        IconButton(onClick = {
                            questionText = ""
                        }) {
                            Icon(
                                Icons.Default.Refresh, contentDescription = null
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (questionText.isNotBlank()) {
                                viewModel.addSomeQuestion(question = questionText)
                                questionText = ""
                            }
                        }) {
                            Icon(
                                Icons.Default.Done, contentDescription = null
                            )
                        }
                    },
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .border(
                            width = 1.dp, color = Color.Black
                        )
                ) {
                    items(playerQuest, key = { quest -> quest.questionId }) { question ->
                        
                        val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { dismissValue ->
                                when (dismissValue) {
                                    SwipeToDismissBoxValue.EndToStart -> {
                                        viewModel.removeQuestion(question = question)
                                        true
                                    }
                                    
                                    SwipeToDismissBoxValue.StartToEnd -> {
                                        
                                        false
                                    }
                                    
                                    else -> false
                                }
                            })
                        
                        SwipeToDismissBox(
                            state = swipeToDismissBoxState, backgroundContent = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.Transparent)
                                ) {
                                    when (swipeToDismissBoxState.dismissDirection) {
                                        SwipeToDismissBoxValue.StartToEnd -> {
                                            Icon(
                                                modifier = Modifier.align(Alignment.CenterStart),
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null
                                            )
                                        }
                                        
                                        SwipeToDismissBoxValue.EndToStart -> {
                                            Icon(
                                                modifier = Modifier.align(Alignment.CenterEnd),
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null
                                            )
                                        }
                                        
                                        SwipeToDismissBoxValue.Settled -> {}
                                    }
                                }
                            }) {
                            QuestionDisplayedUI(question = question)
                        }
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = {
                        viewModel.deletePlayer()
                    }) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
                
            } else {
                
                if (started) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            started = false
                            questionIsShowed = false
                            viewModel.addQuestionToUnresolveList(activePlayerActiveQuestion)
                            viewModel.deleteUsedQuestion(activePlayerActiveQuestion)
                            viewModel.resetActivePlayer()
                        }) {
                            Text(text = "Провал", color = Color.Black)
                        }
                        
                        TimerUI(timerSetTime, onFinish = { onFinishTimer() })
                        
                        TextButton(onClick = {
                            started = false
                            questionIsShowed = false
                            viewModel.addQuestionToResolveList(activePlayerActiveQuestion)
                            viewModel.deleteUsedQuestion(activePlayerActiveQuestion)
                            viewModel.resetActivePlayer()
                        }) {
                            Text(text = "Успех", color = Color.Black)
                        }
                    }
                }
                
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = activePlayerActiveQuestion.question,
                    color = if (!questionIsShowed) Color.White else Color.Black,
                    fontSize = 24.sp
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(30.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {
                        questionIsShowed = !questionIsShowed
                        started = true
                    }) {
                        Text(
                            text = if (!questionIsShowed) "Показать вопрос" else "Скрыть вопрос",
                            color = Color.Black
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                
                IconButton(onClick = {
                    viewModel.resetActivePlayer()
                    started = false
                    questionIsShowed = false
                }) {
                    Icon(Icons.Default.ArrowDropDown, null)
                }
            }
        }
    }
}