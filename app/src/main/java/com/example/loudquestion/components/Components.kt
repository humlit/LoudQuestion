package com.example.loudquestion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay

@Composable
fun QuestionDisplayedUI(question: Question) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = question.question)
        }
        Spacer(modifier = Modifier.height(5.dp))
        
        HorizontalDivider()
    }
}

@Composable
fun ShowCompletedQuestion(
    failedQuestionList: List<Question>,
    successQuestionList: List<Question>,
    isShowed: Boolean,
    onDismiss: () -> Unit
) {
    if (!isShowed) return
    
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Column(
            modifier = Modifier
                .height(500.dp)
                .background(color = Color.White)
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier.weight(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LazyColumn(modifier = Modifier.weight(0.45f)) {
                    items(failedQuestionList, key = { quest -> quest.questionId }) { question ->
                        QuestionDisplayedUI(question)
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                VerticalDivider()
                
                Spacer(modifier = Modifier.width(8.dp))
                
                LazyColumn(modifier = Modifier.weight(0.45f)) {
                    items(successQuestionList, key = { quest -> quest.questionId }) { question ->
                        QuestionDisplayedUI(question)
                    }
                }
            }
        }
    }
}

@Composable
fun TimeEndWarningDialog(
    isShowed: Boolean,
    onDismiss: () -> Unit
) {
    
    if (!isShowed) return
    
    var boxBackgroundColor by remember { mutableStateOf(Color.Red) }
    
    LaunchedEffect(Unit) {
        repeat(100) {
            boxBackgroundColor = if (it % 2 == 0) Color.Red else Color.Blue
            delay(200)
        }
    }
    
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = boxBackgroundColor)
                .clickable(onClick = { onDismiss() })
        )
    }
}