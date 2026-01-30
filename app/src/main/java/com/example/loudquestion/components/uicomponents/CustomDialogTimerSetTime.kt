package com.example.loudquestion.components.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.loudquestion.components.commoncomponents.StyledText
import com.example.loudquestion.ui.theme.Typography
import com.example.loudquestion.viewmodel.MainScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun TimerUI(
    start: Int,
    onFinish: () -> Unit
) {
    var timer by remember { mutableIntStateOf(start) }
    
    LaunchedEffect(timer) {
        if (timer != 0) {
            delay(1000)
            timer--
        } else {
            onFinish()
        }
    }
    
    StyledText(text = "$timer", fontSize = 32.sp)
}

@Composable
fun TimerSetTimeUI(
    listTimerNumbers: List<Int>,
    onSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(listState)
    
    val itemHeight = 40.dp
    
    LaunchedEffect(
        listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset
    ) {
        val index = listState.firstVisibleItemIndex + 2
        onSelected(index)
    }
    
    Box(
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight * 5),
            verticalArrangement = Arrangement.Center,
            state = listState,
            flingBehavior = fling
        ) {
            itemsIndexed(listTimerNumbers) { index, item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    StyledText(text = "$item")
                }
            }
        }
        
        Box(
            modifier = Modifier
                .height(itemHeight)
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .border(width = 2.dp, color = Color.Yellow)
        )
    }
}

@Composable
fun CustomDialogTimerSetTimeTest(
    viewModel: MainScreenViewModel,
    isDialogShowed: Boolean,
    listTimerNumbers: List<Int>,
    onDismiss: () -> Unit,
    onSelected: (Int) -> Unit
) {
    val state by viewModel.gameVM.collectAsState()
    val getTimer = state.timerTime
    
    if (!isDialogShowed) return
    
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier.background(color = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyledText(text = "$getTimer")
                
                TimerSetTimeUI(listTimerNumbers = listTimerNumbers, onSelected = { value ->
                    onSelected(value + 1)
                })
            }
        }
    }
}