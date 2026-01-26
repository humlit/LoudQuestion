package com.example.loudquestion.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.loudquestion.classes.Question

class LoudQuestionViewModelFactory(
    private val application: Application,
    private val questions: List<Question>
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoudQuestionViewModel(application = application, questions = questions) as T
    }
}