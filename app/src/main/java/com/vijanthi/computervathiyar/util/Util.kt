package com.vijanthi.computervathiyar.util

object Util {
    private const val isDev = false
    fun getImageUrl(): String {
        return if (isDev) "https://vi-image-dev.s3.ap-south-1.amazonaws.com/" else "https://vi-image-production.s3.ap-south-1.amazonaws.com/"
    }

    fun getApiUrl(): String {
        return if (isDev) "https://wtyxus64y5.execute-api.ap-south-1.amazonaws.com/dev/" else "https://5jg5wpfcje.execute-api.ap-south-1.amazonaws.com/production/"
    }
}