package com.example.loudquestion.components.commoncomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.loudquestion.ui.theme.LightIndigo
import com.example.loudquestion.ui.theme.LightPurple
import com.example.loudquestion.ui.theme.MidnightBlue

@Preview
@Composable
fun TestUI() {

}

@Composable
fun StyleColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = MidnightBlue)
            .size(100.dp)
            .then(modifier)
    ) {
        content()
    }
}

@Composable
fun StyledPlayerCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = LightIndigo
        ),
        border = BorderStroke(width = 1.dp, color = LightPurple),
        shape = RoundedCornerShape(percent = 20),
        onClick = { onClick() }) {
        content()
    }
}

@Composable
fun StyledRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier, verticalAlignment = verticalAlignment, horizontalArrangement = horizontalArrangement
    ) {
        content()
    }
}

@Composable
fun StyledBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun StyledIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    tint: Color = Color.White
) {
    Icon(modifier = modifier, imageVector = imageVector, contentDescription = contentDescription, tint = tint)
}

@Composable
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(modifier = modifier, text = text, color = color, style = style, fontSize = fontSize)
}