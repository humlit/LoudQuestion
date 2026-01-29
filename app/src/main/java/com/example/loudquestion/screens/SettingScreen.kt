package com.example.loudquestion.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loudquestion.ui.theme.LightIndigo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    playerName: String,
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
        }) { paddingValues ->
        Card(
            modifier = Modifier.fillMaxSize(), colors = CardDefaults.cardColors(containerColor = LightIndigo)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(paddingValues)
                    .padding(start = 35.dp, end = 35.dp, top = 15.dp, bottom = 15.dp)
            ) {
                item {
                    Text(text = playerName, color = Color.White)
                }
            }
        }
    }
}