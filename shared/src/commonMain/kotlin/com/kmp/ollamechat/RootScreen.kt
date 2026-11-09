package com.kmp.ollamechat

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kmp.ollamechat.chatscreen.ChatScreen
import com.kmp.ollamechat.chatscreen.ChatViewModel
import com.kmp.ollamechat.navigation.ChatRoute
import com.kmp.ollamechat.navigation.HomeRoute
import com.kmp.ollamechat.navigation.Route
import com.kmp.ollamechat.homescreen.HomeScreen
import com.kmp.ollamechat.homescreen.HomeViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RootScreen() {
    val config = remember {
        SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) { subclassesOfSealed<Route>() }
            }
        }
    }
    val backStack = rememberNavBackStack(config, HomeRoute)

    // Текущий маршрут (последний в стеке) — для title / bottom bar
    val currentRoute: NavKey? = backStack.lastOrNull()

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            when (currentRoute) {
                                is HomeRoute -> "Home"
                                is ChatRoute -> "Chat"
                                else -> "App"
                            }
                        )
                    },
                    navigationIcon = {
                        if (backStack.size > 1) {
                            IconButton(onClick = { backStack.removeLastOrNull() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute is HomeRoute,
                        onClick = {
                            if (currentRoute !is HomeRoute) {
                                backStack.clear()
                                backStack.add(HomeRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Home") },
                    )

                    NavigationBarItem(
                        selected = currentRoute is ChatRoute,
                        onClick = {
                            if (currentRoute !is ChatRoute) {
                                backStack.clear()
                                backStack.add(ChatRoute)
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null) },
                        label = { Text("Chat") },
                    )

                }
            },
        ) { innerPadding ->
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                modifier = Modifier.padding(innerPadding),   // ← учитываем Scaffold
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<HomeRoute> {
                        val vm: HomeViewModel = koinViewModel()
                        HomeScreen(vm)
                    }
                    entry<ChatRoute> {
                        val vm: ChatViewModel = koinViewModel()
                        ChatScreen(vm)
                    }
                }
            )
        }
    }
}
