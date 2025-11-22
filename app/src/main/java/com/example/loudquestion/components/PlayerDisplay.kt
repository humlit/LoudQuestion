package com.example.loudquestion.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.loudquestion.classes.Player

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PlayerDisplay(
    player: Player,
    onClick: () -> Unit
) {
    BoxWithConstraints {
        Card(
            modifier = Modifier
                .size(maxWidth)
                .padding(2.dp),
            shape = RoundedCornerShape(10),
            border = BorderStroke(width = 1.dp, color = Color.Black),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                onClick()
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 15.dp),
            ) {
                Text(text = player.playerName)
            }
        }
    }
}