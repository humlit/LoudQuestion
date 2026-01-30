package com.example.loudquestion.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loudquestion.classes.Player
import com.example.loudquestion.components.uicomponents.PlayersListUI
import com.example.loudquestion.ui.theme.LightIndigo
import com.example.loudquestion.ui.theme.LightPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    playersList: List<Player>,
    onBackPressedButton: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Настройки", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = LightIndigo),
                navigationIcon = {
                    IconButton(onClick = { onBackPressedButton() }) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = null, tint = Color.White
                        )
                    }
                })
        }, containerColor = LightIndigo
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(paddingValues)
                .padding(start = 35.dp, end = 35.dp, top = 15.dp, bottom = 15.dp),
            color = LightIndigo,
            shape = RoundedCornerShape(10),
            border = BorderStroke(
                width = 1.dp, color = LightPurple
            )
        ) {
            LazyColumn() {
                items(playersList, key = { player -> player.playerId }) { player ->
                    PlayersListUI(player = player)
                }
            }
        }
    }
}