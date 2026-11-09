package com.kmp.ollamechat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform