package com.example.loudquestion.screens

import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.loudquestion.components.CustomModalNavigationDrawer
import com.example.loudquestion.components.MainPlayerUI
import com.example.loudquestion.viewmodel.LoudQuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: LoudQuestionViewModel) {
    val state by viewModel.gameVM.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    CustomModalNavigationDrawer(
        viewModel = viewModel, drawerState = drawerState, gameStatusBoolean = state.isGameStart
    ) {
        MainPlayerUI(
            viewModel = viewModel,
            gameIsStarted = state.isGameStart,
            unresolvedAnswerList = state.unresolvedQuestion,
            resolvedAnswerList = state.resolvedQuestion,
            drawerState = drawerState,
            playerList = state.playerList
        )
        
        Log.d("CHECKERS_PLAYERS",state.playerList.toString())
        Log.d("CHECKERS_RESOLVE",state.resolvedQuestion.toString())
        Log.d("CHECKERS_UNRESOLVE",state.unresolvedQuestion.toString())
    }
}