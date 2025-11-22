package com.example.loudquestion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loudquestion.viewmodel.LoudQuestionViewModel

@Composable
fun CustomModalNavigationDrawer(
    viewModel: LoudQuestionViewModel,
    gameStatusBoolean: Boolean,
    drawerState: DrawerState,
    content: @Composable () -> Unit
) {
    val state by viewModel.gameVM.collectAsState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var isDialogShowed by remember { mutableStateOf(false) }
    
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
                NavigationDrawerItem(modifier = Modifier.padding(top = 40.dp), label = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Игра активна")
                        Switch(
                            checked = gameStatusBoolean,
                            onCheckedChange = { it == gameStatusBoolean })
                    }
                }, selected = false, onClick = { viewModel.changeGameStatus() })
                
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
                }
            }
        }) { content() }
}