package com.example.loudquestion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.loudquestion.navigation.AppNavGraph
import com.example.loudquestion.navigation.rememberNavigationState
import com.example.loudquestion.ui.theme.LoudQuestionTheme
import com.example.loudquestion.viewmodel.MainScreenViewModel

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoudQuestionTheme {
                val navigationState = rememberNavigationState()
                AppNavGraph(navHostController = navigationState.navHostController)
            }
        }
    }
}
