package com.nanotricks.techysaw.domain.model

import com.nanotricks.techysaw.data.model.HNItem

// Preferring composition over inheritance
data class HNItemWithTimeAgo(
    val item: HNItem,
    val timeAgo: String
)