package com.example.loudquestion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.loudquestion.screens.MainScreen
import com.example.loudquestion.ui.theme.LoudQuestionTheme
import com.example.loudquestion.viewmodel.LoudQuestionViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: LoudQuestionViewModel by viewModels<LoudQuestionViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoudQuestionTheme {
                MainScreen(viewModel)
            }
        }
    }
}
