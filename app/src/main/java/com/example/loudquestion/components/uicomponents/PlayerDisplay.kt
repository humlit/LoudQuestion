package com.example.loudquestion.components.uicomponents

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loudquestion.classes.Player
import com.example.loudquestion.components.commoncomponents.StyledCard

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PlayerDisplay(
    player: Player,
    onClick: () -> Unit
) {
    BoxWithConstraints {
        StyledCard(
            modifier = Modifier.size(width = maxWidth, height = 150.dp),
            onClick = { onClick() }) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PlayerCardColumn(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(modifier = Modifier.fillMaxSize(), imageVector = Icons.Default.AccountCircle, contentDescription = null)
                }
                
                PlayerCardColumn(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Text(text = player.playerName, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun PlayerCardColumn(
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