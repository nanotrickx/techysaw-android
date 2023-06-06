package com.vijanthi.computervathiyar.data.model

import com.google.gson.annotations.SerializedName
import work.upstarts.editorjskit.models.EJBlock

data class EJResponse(
    @SerializedName("time") var time: Int? = null,
    @SerializedName("version") var version: String? = null,
    val blocks: List<EJBlock>
)