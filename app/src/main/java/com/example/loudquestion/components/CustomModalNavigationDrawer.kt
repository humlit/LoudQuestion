package com.example.loudquestion.components

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loudquestion.viewmodel.LoudQuestionViewModel

@Composable
fun CustomModalNavigationDrawer(
    viewModel: LoudQuestionViewModel,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val state by viewModel.gameVM.collectAsState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var isDialogShowed by remember { mutableStateOf(false) }
    var isStateEditShowed by remember { mutableStateOf(false) }
    
    CustomDialogPlayerAdd(viewModel = viewModel, isDialogShowed = isDialogOpen, onDismiss = {
        isDialogOpen = false
    }, onConfirm = {
        isDialogOpen = false
    })
    
    CustomDialogTimerSetTime(
        viewModel = viewModel,
        isDialogShowed = isDialogShowed,
        onDismiss = { isDialogShowed = false },
        onConfirm = { isDialogShowed = false })
    
    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(250.dp)
            ) {
                Text(modifier = Modifier.padding(16.dp), text = "Управление", fontSize = 24.sp)
                
                NavigationDrawerItem(
                    label = { Text(text = "Игра активна") },
                    selected = false,
                    onClick = {},
                    badge = {
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
                
                HorizontalDivider()
                
                if (!state.isGameStart) {
                    NavigationDrawerItem(
                        label = { Text(text = "Добавить игрока") },
                        selected = false,
                        onClick = {
                            isDialogOpen = true
                        })
                    
                    HorizontalDivider()
                    
                    NavigationDrawerItem(
                        label = { Text(text = "Установить время") },
                        selected = false,
                        onClick = {
                            isDialogShowed = true
                        })
                    
                    HorizontalDivider()
                    
                    NavigationDrawerItem(label = {
                        Text(text = "Управление игрой")
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
                            contentDescription = null
                        )
                    })
                    
                    AnimatedVisibility(isStateEditShowed) {
                        Column {
                            HorizontalDivider()
                          
                            NavigationDrawerItem(label = {
                                Text(text = "Очистить завершённые вопросы")
                            }, selected = false, onClick = {
                                viewModel.clearCompletedQuestion()
                            })
                            
                            NavigationDrawerItem(label = {
                                Text(text = "Сброс игры")
                            }, selected = false, onClick = {
                                viewModel.resetGameState()
                            })
                            
                            HorizontalDivider()
                        }
                    }
                }
            }
        }) {
        content()
    }
}