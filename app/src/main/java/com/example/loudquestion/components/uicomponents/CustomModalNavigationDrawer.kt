package com.example.loudquestion.components.uicomponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.loudquestion.components.commoncomponents.StyledText
import com.example.loudquestion.ui.theme.LightIndigo
import com.example.loudquestion.ui.theme.Typography
import com.example.loudquestion.viewmodel.MainScreenViewModel

@Composable
fun CustomModalNavigationDrawer(
    viewModel: MainScreenViewModel,
    drawerState: DrawerState,
    onSettingButtonPressed: () -> Unit,
    content: @Composable () -> Unit
) {
    val state by viewModel.gameVM.collectAsState()
    var isPlayerAddDialogOpen by remember { mutableStateOf(false) }
    var isTimerSetTimeDialogShowed by remember { mutableStateOf(false) }
    var isStateEditShowed by remember { mutableStateOf(false) }
    var selectedTimerTime by remember { mutableIntStateOf(state.timerTime) }
    
    CustomDialogPlayerAdd(isDialogShowed = isPlayerAddDialogOpen, onDismiss = {
        isPlayerAddDialogOpen = false
    }, onConfirm = {
        isPlayerAddDialogOpen = false
    }, addPlayer = { playerName, playerImage -> viewModel.addPlayer(playerName = playerName, playerImage = playerImage) })
    
    val listOfNumbersForTimer = mutableListOf<Int>().apply {
        repeat(130) {
            add(it + 1)
        }
    }.filter { it % 5 == 0 }
    
    CustomDialogTimerSetTimeTest(
        viewModel = viewModel,
        isDialogShowed = isTimerSetTimeDialogShowed,
        listTimerNumbers = listOfNumbersForTimer,
        onDismiss = {
            viewModel.timerSetTimes(selectedTimerTime)
            isTimerSetTimeDialogShowed = false
        },
        onSelected = { index ->
            selectedTimerTime = index * 5
        })
    
    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp),
                drawerContainerColor = LightIndigo,
            ) {
                StyledText(modifier = Modifier.padding(16.dp), text = "Управление", style = Typography.labelLarge)
                
                NavigationDrawerItem(label = { StyledText(text = "Игра активна") }, selected = false, onClick = {}, badge = {
                    Switch(
                        checked = state.isGameStart, onCheckedChange = { newValue ->
                            viewModel.changeGameStatus(newValue)
                        }, colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.White,
                            checkedTrackColor = Color.Green,
                            uncheckedTrackColor = Color.Red,
                            checkedBorderColor = Color.Transparent,
                            uncheckedBorderColor = Color.Transparent
                        )
                    )
                })
                AnimatedVisibility(!state.isGameStart) {
                    Column {
                        HorizontalDivider(color = Color.Black)
                        
                        NavigationDrawerItem(label = { StyledText(text = "Добавить игрока") }, selected = false, onClick = {
                            isPlayerAddDialogOpen = true
                        })
                        
                        HorizontalDivider(color = Color.Black)
                        
                        NavigationDrawerItem(label = { StyledText(text = "Установить таймер") }, selected = false, onClick = {
                            isTimerSetTimeDialogShowed = true
                        }, badge = { StyledText(text = "${state.timerTime}") })
                        
                        HorizontalDivider(color = Color.Black)
                        
                        NavigationDrawerItem(label = {
                            StyledText(text = "Управление игрой")
                        }, selected = false, onClick = {
                            isStateEditShowed = !isStateEditShowed
                        }, badge = {
                            val arrowScale by animateFloatAsState(
                                targetValue = if (!isStateEditShowed) 1f else -1f,
                                animationSpec = tween(durationMillis = 300),
                            )
                            
                            Icon(
                                modifier = Modifier.graphicsLayer(scaleY = arrowScale),
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = Color.White
                            )
                        })
                        
                        AnimatedVisibility(isStateEditShowed) {
                            Column {
                                var isCompletedQuestionAlertDialogShowed by remember {
                                    mutableStateOf(
                                        false
                                    )
                                }
                                var isGameResetAlertDialogShowed by remember { mutableStateOf(false) }
                                
                                HorizontalDivider(color = Color.Black)
                                
                                NavigationDrawerItem(label = {
                                    StyledText(text = "Очистить завершённые вопросы")
                                }, selected = false, onClick = {
                                    isCompletedQuestionAlertDialogShowed = true
                                })
                                
                                NavigationDrawerItem(label = {
                                    StyledText(text = "Сброс игры")
                                }, selected = false, onClick = {
                                    isGameResetAlertDialogShowed = true
                                })
                                
                                CustomAlertDialog(
                                    isAlertDialogShowed = isCompletedQuestionAlertDialogShowed,
                                    onDismiss = {
                                        isCompletedQuestionAlertDialogShowed = false
                                    },
                                    onConfirm = {
                                        viewModel.clearCompletedQuestion()
                                        isCompletedQuestionAlertDialogShowed = false
                                    },
                                )
                                
                                CustomAlertDialog(
                                    isAlertDialogShowed = isGameResetAlertDialogShowed,
                                    onDismiss = {
                                        isGameResetAlertDialogShowed = false
                                    },
                                    onConfirm = {
                                        viewModel.resetGameState()
                                        isGameResetAlertDialogShowed = false
                                    },
                                )
                            }
                        }
                        
                        HorizontalDivider(color = Color.Black)
                        
                        NavigationDrawerItem(label = {
                            StyledText(text = "Настройки")
                        }, selected = false, onClick = { onSettingButtonPressed() })
                    }
                }
            }
        }) {
        content()
    }
}