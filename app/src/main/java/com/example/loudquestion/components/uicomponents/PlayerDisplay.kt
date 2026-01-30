package com.example.loudquestion.components.uicomponents

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.loudquestion.classes.Player
import com.example.loudquestion.components.commoncomponents.StyledPlayerCard
import com.example.loudquestion.components.commoncomponents.StyledText

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PlayerDisplay(
    player: Player,
    onClick: () -> Unit
) {
    BoxWithConstraints {
        StyledPlayerCard(
            modifier = Modifier.size(width = maxWidth, height = 150.dp), onClick = { onClick() }) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                PlayerCardColumn(
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = painterResource(id = player.playerImage),
                        contentDescription = null,
                        alignment = Alignment.Center,
                        contentScale = ContentScale.Inside
                    )
                }
                
                PlayerCardColumn(
                    modifier = Modifier.weight(0.5f)
                ) {
                    StyledText(text = player.playerName)
                }
            }
        }
    }
}