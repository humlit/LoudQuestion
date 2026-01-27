package com.example.loudquestion.components.commoncomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loudquestion.ui.theme.LightIndigo
import com.example.loudquestion.ui.theme.LightPurple
import com.example.loudquestion.ui.theme.MidnightBlue

@Preview
@Composable
fun TestUI() {
    StyleColumn() {}
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
fun StyledCard(
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
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.then(modifier)
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
        modifier = Modifier.then(modifier)
    ) {
        content()
    }
}

@Composable
fun StyledText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Text(modifier = Modifier.then(modifier), text = text, color = Color.White, style = style)
}