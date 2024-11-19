package baltiapps.callblock.ui.screens.appScreen.homeSettings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import baltiapps.callblock.MainActivity
import baltiapps.callblock.R
import baltiapps.callblock.domain.UIScope
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeSettings(modifier: Modifier = Modifier) {
    val viewModel: HomeSettingsViewModel = koinViewModel<HomeSettingsViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current

    val state: HomeSettingsState by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner,
    )

    val context = LocalContext.current as? MainActivity

    Content(
        state = state,
        uiScope = context,
        performAction = viewModel::performAction,
        modifier = modifier
    )
}

@Composable
private fun Content(
    state: HomeSettingsState,
    uiScope: UIScope?,
    performAction: (HomeSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Surface(
        modifier = modifier.fillMaxSize().verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            SwitchItem(
                label = stringResource(R.string.silence_unknown_calls),
                isChecked = state.isSilenceEnabled,
                onSwitchToggled = {
                    uiScope?.run {
                        performAction(HomeSettingsAction.ToggleSilenceCalls(this))
                    }
                }
            )
            SwitchItem(
                label = stringResource(R.string.block_unknown_calls),
                isChecked = state.isBlockEnabled,
                onSwitchToggled = {
                    uiScope?.run {
                        performAction(HomeSettingsAction.ToggleBlockCalls(this))
                    }
                }
            )
            SwitchItem(
                label = stringResource(R.string.enable_notifications),
                isChecked = state.isNotificationsEnabled,
                onSwitchToggled = {
                    uiScope?.run {
                        performAction(HomeSettingsAction.ToggleNotifications(this))
                    }
                }
            )
        }
    }
}

@Composable
private fun SwitchItem(
    label: String,
    isChecked: Boolean,
    onSwitchToggled: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSwitchToggled()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label)
        Switch(
            checked = isChecked,
            onCheckedChange = null,
        )
    }
}

@Preview
@Composable
private fun ContentPreview(modifier: Modifier = Modifier) {
    Content(
        state = HomeSettingsState(
            isSilenceEnabled = false,
            isBlockEnabled = false,
            isNotificationsEnabled = false,
            isSystemNotificationsEnabled = true,
        ),
        uiScope = null,
        performAction = {}
    )
}