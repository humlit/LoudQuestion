package com.example.loudquestion.components.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.loudquestion.ui.theme.AppDrawables

@Composable
fun CustomDialogPlayerAdd(
    isDialogShowed: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    addPlayer: (String, Int) -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    val playerImageList = listOf(
        AppDrawables.Winking,
        AppDrawables.ManPeople,
        AppDrawables.Singer,
        AppDrawables.Ninja,
        AppDrawables.PersonBowing,
        AppDrawables.GirlHandoff,
        AppDrawables.Chinese,
        AppDrawables.WomanPeople
    )
    var currentPlayerImage by remember { mutableIntStateOf(playerImageList[0]) }
    
    if (!isDialogShowed) return
    
    Dialog(onDismissRequest = {
        onDismiss()
        playerName = ""
    }) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
                .padding(8.dp)
        ) {
            TextField(
                value = playerName, onValueChange = { playerName = it }, colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Black,
                    unfocusedIndicatorColor = Color.Black
                ), singleLine = true, placeholder = { Text(text = "Имя игрока") })
            
            Spacer(modifier = Modifier.height(10.dp))
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items(playerImageList, key = { image -> image }) { image ->
                    PlayerImageUI(
                        playerImage = image,
                        isSelected = currentPlayerImage == image,
                        onPlayerImageClick = { currentPlayerImage = image })
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    onDismiss()
                    playerName = ""
                }) {
                    Icon(
                        Icons.Default.Close, contentDescription = null
                    )
                }
                
                IconButton(onClick = {
                    if (playerName.isNotBlank()) {
                        addPlayer(playerName, currentPlayerImage)
                        currentPlayerImage = playerImageList[0]
                        playerName = ""
                        onConfirm()
                    }
                }) {
                    Icon(
                        Icons.Default.Done, contentDescription = null
                    )
                }
            }
        }
    }
}