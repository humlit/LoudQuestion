package com.example.loudquestion.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
        title = { Text(text = "Подтвердите действие") },
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = dismissButtonText)
                }
                
                TextButton(onClick = { onConfirm() }) {
                    Text(text = confirmButtonText)
                }
            }
        },
    )
}