package com.example.loudquestion.components.uicomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.loudquestion.R
import com.example.loudquestion.classes.Player
import com.example.loudquestion.classes.Question
import com.example.loudquestion.components.commoncomponents.StyledText
import com.example.loudquestion.ui.theme.LightIndigo
import com.example.loudquestion.ui.theme.Typography
import kotlinx.coroutines.delay

@Preview
@Composable
fun Test() {
    PlayersListUI(
        Player(
            playerName = "Mamat", playerImage = R.drawable.ic_winking_laught_emotion_face, playerQuestion = listOf(
                Question(question = "1"),
                Question(question = "2"),
                Question(question = "3"),
            )
        )
    )
}

@Composable
fun PlayersListUI(player: Player) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(modifier = Modifier.size(30.dp), painter = painterResource(player.playerImage), contentDescription = null)
            
            Spacer(modifier = Modifier.width(10.dp))
            
            StyledText(text = player.playerName)
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            StyledText(text = "${player.playerQuestion.count()}")
            
            Spacer(modifier = Modifier.width(10.dp))
            
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun PlayerImageUI(
    playerImage: Int,
    isSelected: Boolean = false,
    onPlayerImageClick: () -> Unit
) {
    val isSelectedBorderColor = if (isSelected) Color.Green else Color.Black
    
    Card(
        modifier = Modifier
            .size(70.dp)
            .background(color = Color.Transparent)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(width = 1.dp, color = isSelectedBorderColor),
        onClick = { onPlayerImageClick() }) {
        Image(painter = painterResource(id = playerImage), contentDescription = null)
    }
}

@Composable
fun QuestionDisplayedUI(
    question: Question,
    onConfirmEditingQuestionText: (Question) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            var questionText by remember(question.questId, question.question) { mutableStateOf(question.question) }
            val questionReadOnlyStatus = question.isReadOnly
            
            val containerColor = if (questionReadOnlyStatus) Color.Transparent else Color.LightGray
            val textFieldColors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor, unfocusedContainerColor = containerColor
            )
            
            TextField(
                value = questionText, onValueChange = { questionText = it }, readOnly = questionReadOnlyStatus, trailingIcon = {
                if (!questionReadOnlyStatus) {
                    IconButton(onClick = {
                        val newQuestion = Question(
                            questId = question.questId, question = questionText, isReadOnly = questionReadOnlyStatus
                        )
                        onConfirmEditingQuestionText(newQuestion)
                    }) { Icon(Icons.Default.Done, contentDescription = null) }
                }
            }, colors = textFieldColors
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}

@Composable
fun AskedQuestionUI(question: Question) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            StyledText(text = question.question, style = Typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(5.dp))
        
        HorizontalDivider()
    }
}

@Composable
fun PlayerCardColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(all = 5.dp)
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
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
                .background(color = LightIndigo)
                .padding(8.dp),
        ) {
            Row(
                modifier = Modifier.weight(0.9f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                LazyColumn(modifier = Modifier.weight(0.45f)) {
                    items(failedQuestionList, key = { quest -> quest.questId }) { question ->
                        AskedQuestionUI(question)
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                VerticalDivider()
                
                Spacer(modifier = Modifier.width(8.dp))
                
                LazyColumn(modifier = Modifier.weight(0.45f)) {
                    items(successQuestionList, key = { quest -> quest.questId }) { question ->
                        AskedQuestionUI(question)
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