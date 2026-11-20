package com.kmp.ollamechat.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey
@Serializable
data object SettingsRoute : Route

@Serializable
data object HomeRoute: Route

@Serializable
data object ChatRoute : Route
