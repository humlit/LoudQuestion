package com.example.loudquestion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.loudquestion.screens.MainScreen
import com.example.loudquestion.viewmodel.LoudQuestionViewModel
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<LoudQuestionViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen(viewModel)
        }
    }
}
