package com.nanotricks.techysaw.ui.common

import android.view.GestureDetector
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(onClick: () -> Unit) {

    Box(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp).background(
            Color.White, shape = CircleShape.copy(CornerSize(16.dp))
        ).border(
            BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f)),shape = CircleShape.copy(
                CornerSize(8.dp)
            )).clickable {
                         onClick()
        },
        contentAlignment = Alignment.Center
    ) {
        Icon(Icons.Filled.PlayArrow,"", modifier = Modifier.rotate(180F).padding(4.dp))
    }
}