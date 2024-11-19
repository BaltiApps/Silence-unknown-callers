package baltiapps.callblock.ui.screens.appScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import baltiapps.callblock.R
import baltiapps.callblock.ui.screens.appScreen.callLogs.CallLogs
import baltiapps.callblock.ui.screens.appScreen.homeSettings.HomeSettings
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppScreen(
    navigateToFirstSetup: () -> Unit
) {
    val viewModel = koinViewModel<AppScreenViewModel>()

    if (!viewModel.isFirstSetupDone()) {
        navigateToFirstSetup()
        return
    }

    val navController = rememberNavController()
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(RouteHomeSettings.index)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedIndex == RouteHomeSettings.index,
                    onClick = {
                        selectedIndex = RouteHomeSettings.index
                        navController.navigate(RouteHomeSettings) {
                            popUpTo(navController.graph.id)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selectedIndex == RouteHomeSettings.index) {
                                Icons.Filled.Settings
                            } else Icons.Outlined.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    },
                    label = {
                        Text(stringResource(R.string.settings))
                    }
                )
                NavigationBarItem(
                    selected = selectedIndex == RouteLogs.index,
                    onClick = {
                        selectedIndex = RouteLogs.index
                        navController.navigate(RouteLogs) {
                            popUpTo(navController.graph.id)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selectedIndex == RouteHomeSettings.index) {
                                Icons.AutoMirrored.Filled.List
                            } else Icons.AutoMirrored.Outlined.List,
                            contentDescription = stringResource(R.string.call_logs)
                        )
                    },
                    label = {
                        Text(stringResource(R.string.call_logs))
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RouteHomeSettings,
        ) {
            composable<RouteHomeSettings> {
                selectedIndex = RouteHomeSettings.index
                HomeSettings(
                    modifier = Modifier.padding(innerPadding).safeDrawingPadding()
                )
            }
            composable<RouteLogs> {
                selectedIndex = RouteLogs.index
                CallLogs(
                    modifier = Modifier.safeDrawingPadding()
                )
            }
        }
    }
}

interface ScreenRoute {
    val index: Int
}

@Serializable
object RouteHomeSettings: ScreenRoute {
    override val index: Int = 2
}

@Serializable
object RouteLogs: ScreenRoute {
    override val index: Int = 3
}