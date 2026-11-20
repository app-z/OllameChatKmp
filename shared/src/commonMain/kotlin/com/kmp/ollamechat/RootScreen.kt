package com.kmp.ollamechat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.kmp.ollamechat.chat.ChatScreen
import com.kmp.ollamechat.chat.ChatViewModel
import com.kmp.ollamechat.generate.GenerateScreen
import com.kmp.ollamechat.generate.GenerateViewModel
import com.kmp.ollamechat.home.HomeScreen
import com.kmp.ollamechat.home.HomeViewModel
import com.kmp.ollamechat.navigation.ChatRoute
import com.kmp.ollamechat.navigation.GenerateRoute
import com.kmp.ollamechat.navigation.HomeRoute
import com.kmp.ollamechat.navigation.Route
import com.kmp.ollamechat.navigation.SettingsRoute
import com.kmp.ollamechat.settings.SettingsScreen
import com.kmp.ollamechat.settings.SettingsViewModel
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

    MaterialTheme {


        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    val vm: HomeViewModel = koinViewModel()
                    HomeScreen( viewModel = vm,
                        onChatRoute = {
                            backStack.add(ChatRoute)
                        },
                        onGenerateRoute = {
                            backStack.add(GenerateRoute)
                        }
                    )
                }

                entry<SettingsRoute> {
                    val vm: SettingsViewModel = koinViewModel()
                    SettingsScreen(
                        viewModel = vm,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
                entry<GenerateRoute> {
                    val vm: GenerateViewModel = koinViewModel()
                    GenerateScreen(
                        viewModel = vm,
                        onBack = { backStack.removeLastOrNull() },

                        )
                }
                entry<ChatRoute> {
                    val vm: ChatViewModel = koinViewModel()
                    ChatScreen(
                        viewModel = vm,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }
            }
        )
    }
}

//@Composable
//@Preview
//fun RootScreenPreview() {
//    RootScreen()
//}