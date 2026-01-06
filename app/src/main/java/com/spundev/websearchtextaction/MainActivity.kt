package com.spundev.websearchtextaction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.spundev.websearchtextaction.transition.materialSharedAxisIn
import com.spundev.websearchtextaction.transition.materialSharedAxisOut
import com.spundev.websearchtextaction.ui.screens.browserConfig.BrowserConfigRoute
import com.spundev.websearchtextaction.ui.screens.modePicker.ModePickerRoute
import com.spundev.websearchtextaction.ui.theme.WebSearchTextActionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object ModePickerRoute : NavKey

@Serializable
object BrowserConfigRoute : NavKey

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebSearchTextActionTheme {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    NavProvider()
                }
            }
        }
    }
}

@Composable
private fun NavProvider() {
    val density = LocalDensity.current
    val backStack = rememberNavBackStack(ModePickerRoute)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<ModePickerRoute> {
                ModePickerRoute(
                    onBrowserConfig = {
                        backStack.add(BrowserConfigRoute)
                    }
                )
            }
            entry<BrowserConfigRoute> {
                BrowserConfigRoute(
                    onBack = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        },
        transitionSpec = {
            // Slide in from right when navigating forward
            materialSharedAxisIn(
                slideDirection = AnimatedContentTransitionScope.SlideDirection.Left,
                density = density
            ) togetherWith materialSharedAxisOut(
                slideDirection = AnimatedContentTransitionScope.SlideDirection.Left,
                density = density
            )
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            materialSharedAxisIn(
                slideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
                density = density
            ) togetherWith materialSharedAxisOut(
                slideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
                density = density
            )
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            materialSharedAxisIn(
                slideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
                density = density
            ) togetherWith materialSharedAxisOut(
                slideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
                density = density
            )
        },
    )
}
