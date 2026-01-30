package com.example.loudquestion.components.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
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
import androidx.compose.ui.window.Dialog
import com.example.loudquestion.classes.Question
import com.example.loudquestion.components.commoncomponents.StyledIcon
import com.example.loudquestion.components.commoncomponents.StyledText
import com.example.loudquestion.ui.theme.LightIndigo
import com.example.loudquestion.ui.theme.LightPurple
import com.example.loudquestion.ui.theme.Typography
import com.example.loudquestion.usercase.choiceOfQuestion
import com.example.loudquestion.viewmodel.MainScreenViewModel

@Composable
fun DisplayPlayerInfo(
    viewModel: MainScreenViewModel,
    isDialogShowed: Boolean,
    onFinishTimer: () -> Unit
) {
    val state by viewModel.gameVM.collectAsState()
    val activePlayer = state.activePlayer
    val playerList = state.playerList
    val timerSetTime = state.timerTime
    
    var questionText by remember { mutableStateOf("") }
    val playerQuest = activePlayer?.playerQuestion ?: emptyList()
    var questionIsShowed by remember { mutableStateOf(false) }
    var started by remember { mutableStateOf(false) }
    
    if (!isDialogShowed) return
    
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .background(color = LightIndigo)
                .padding(12.dp)
        ) {
            if (!state.isGameStart) {
                TextField(
                    value = questionText,
                    onValueChange = { questionText = it },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White
                    ),
                    placeholder = { StyledText(text = "Какой вопрос?") },
                    leadingIcon = {
                        IconButton(onClick = {
                            questionText = ""
                        }) {
                            StyledIcon(imageVector = Icons.Default.Refresh)
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (questionText.isNotBlank()) {
                                viewModel.addSomeQuestion(question = questionText)
                                questionText = ""
                            }
                        }) {
                            StyledIcon(imageVector = Icons.Default.Done)
                        }
                    },
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    color = LightIndigo,
                    border = BorderStroke(1.dp, LightPurple),
                    shape = RoundedCornerShape(10)
                ) {
                    LazyColumn() {
                        items(playerQuest, key = { quest -> quest.questId }) { question ->
                            
                            val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                                confirmValueChange = { dismissValue ->
                                    when (dismissValue) {
                                        SwipeToDismissBoxValue.EndToStart -> {
                                            viewModel.removeQuestion(question = question)
                                            true
                                        }
                                        
                                        SwipeToDismissBoxValue.StartToEnd -> {
                                            viewModel.changeReadOnlyStatusOnQuestion(question = question)
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
                                QuestionDisplayedUI(question = question, onConfirmEditingQuestionText = { question ->
                                    viewModel.editingQuestionText(question)
                                    viewModel.changeReadOnlyStatusOnQuestion(question)
                                })
                            }
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
                        StyledIcon(Icons.Default.Delete)
                    }
                }
                
            } else {
                val randomQuestion = remember {
                    choiceOfQuestion(
                        activePlayer = activePlayer, playerList = playerList
                    ) ?: Question(question = "Вопросы кончились")
                }
                
                if (started) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = {
                            started = false
                            questionIsShowed = false
                            viewModel.resetActivePlayer()
                            viewModel.unresolveQuestion(randomQuestion)
                            viewModel.deleteUsedQuestion(randomQuestion)
                        }) {
                            StyledText(text = "Провал")
                        }
                        
                        TimerUI(timerSetTime, onFinish = { onFinishTimer() })
                        
                        TextButton(onClick = {
                            started = false
                            questionIsShowed = false
                            viewModel.resetActivePlayer()
                            viewModel.resolveQuestion(randomQuestion)
                            viewModel.deleteUsedQuestion(randomQuestion)
                        }) {
                            StyledText(text = "Успех")
                        }
                    }
                }
                
                TextField(
                    value = randomQuestion.question,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = false,
                    enabled = false,
                    textStyle = Typography.labelLarge,
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.Transparent,
                        disabledIndicatorColor = LightPurple,
                        disabledTextColor = if(questionIsShowed) Color.White else Color.Transparent,
                    )
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(onClick = {
                        questionIsShowed = !questionIsShowed
                        started = true
                    }) {
                        StyledText(
                            text = if (!questionIsShowed) "Показать вопрос" else "Скрыть вопрос"
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
                    StyledIcon(Icons.Default.ArrowDropDown)
                }
            }
        }
    }
}