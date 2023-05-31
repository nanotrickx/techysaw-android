package com.vijanthi.computervathiyar.domain.model

import com.vijanthi.computervathiyar.data.model.HNItem

// Preferring composition over inheritance
data class HNItemWithTimeAgo(
    val item: HNItem,
    val timeAgo: String
)