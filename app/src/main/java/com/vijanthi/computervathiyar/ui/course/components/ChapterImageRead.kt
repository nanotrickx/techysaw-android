package com.vijanthi.computervathiyar.ui.course.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vijanthi.computervathiyar.R

@Composable
fun ChapterImageRead() {
    Image(
        painter = painterResource(id = R.drawable.ic_check_blue),
        modifier = Modifier.size(30.dp),
        contentDescription = ""
    )
}

@Composable
fun ChapterImageUnread() {
    Image(
        painter = painterResource(id = R.drawable.ic_check_grey),
        modifier = Modifier.size(30.dp),
        contentDescription = ""
    )
}