package com.example.loudquestion.components.uicomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.loudquestion.components.commoncomponents.StyledRow
import com.example.loudquestion.components.commoncomponents.StyledText
import com.example.loudquestion.ui.theme.LightIndigo

@Composable
fun CustomAlertDialog(
    isAlertDialogShowed: Boolean,
    dismissButtonText: String = "Отменить",
    confirmButtonText: String = "Подтвердить",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (!isAlertDialogShowed) return
    
    AlertDialog(
        title = { StyledText(text = "Подтвердите действие") },
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color.White) },
        containerColor = LightIndigo,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            StyledRow(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { onDismiss() }) {
                    StyledText(text = dismissButtonText)
                }
                
                TextButton(onClick = { onConfirm() }) {
                    StyledText(text = confirmButtonText)
                }
            }
        },
    )
}