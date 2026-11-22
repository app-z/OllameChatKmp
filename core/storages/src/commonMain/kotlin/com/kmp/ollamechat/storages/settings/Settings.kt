package com.kmp.ollamechat.storages.settings

import kotlin.time.Duration.Companion.milliseconds

class Settings {
    fun getIntervalForSaveMessageToDb() = 250.milliseconds
}