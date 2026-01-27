package com.example.loudquestion.screens

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.loudquestion.components.uicomponents.CustomModalNavigationDrawer
import com.example.loudquestion.components.uicomponents.MainPlayerUI
import com.example.loudquestion.viewmodel.LoudQuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: LoudQuestionViewModel) {
    val state by viewModel.gameVM.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(state) {
        viewModel.setContext()
    }
    
    CustomModalNavigationDrawer(
        viewModel = viewModel, drawerState = drawerState,
    ) {
        MainPlayerUI(
            viewModel = viewModel,
            gameIsStarted = state.isGameStart,
            drawerState = drawerState,
            playerList = state.playerList
        )
    }
    
    Log.d("MAIN_CHECKER_PLAYERS", state.playerList.toString())
    Log.d("MAIN_CHECKER_UNR_QUESTION", state.unresolvedQuestion.toString())
    Log.d("MAIN_CHECKER_R_QUESTION", state.resolvedQuestion.toString())
}