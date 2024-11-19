package baltiapps.callblock.ui.screens.appScreen.callLogs

import android.provider.CallLog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMade
import androidx.compose.material.icons.automirrored.filled.CallReceived
import androidx.compose.material.icons.automirrored.filled.PhoneMissed
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.SettingsPhone
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import baltiapps.callblock.MainActivity
import baltiapps.callblock.R
import baltiapps.callblock.domain.UIScope
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.models.FilterVerdict
import baltiapps.callblock.utils.convertLongToTimestamp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CallLogs(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<CallLogsViewModel>()
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by viewModel.state.collectAsStateWithLifecycle(
        lifecycleOwner = lifecycleOwner
    )

    val context = LocalContext.current as? MainActivity

    Content(
        state = { state },
        uiScope = context,
        performAction = viewModel::performAction,
        modifier = modifier
    )
}

@Composable
private fun Content(
    state: () -> CallLogsState,
    uiScope: UIScope?,
    performAction: (CallLogsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!state().isCallLogPermissionGranted) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1F).padding(4.dp),
                            text = stringResource(R.string.call_logs_permission_not_allowed)
                        )
                        Button(
                            onClick = {
                                uiScope?.run {
                                    performAction(
                                        CallLogsAction.RequestCallLogPermission(this)
                                    )
                                }
                            }
                        ) {
                            Text(stringResource(R.string.allow))
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(1F)
            ) {
                if (state().isLoadingCallLogs) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else if (state().callInformationList.isEmpty()) {
                    Column (
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(stringResource(R.string.nothing_to_show))
                        Text(stringResource(R.string.nothing_to_show_desc))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state().callInformationList) {
                            CallLogItem(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CallLogItem(
    callInformation: CallInformation,
) {
    ListItem(
        leadingContent = {
            Image(
                imageVector = when (callInformation.callType) {
                    CallLog.Calls.MISSED_TYPE -> Icons.AutoMirrored.Filled.PhoneMissed
                    CallLog.Calls.INCOMING_TYPE -> Icons.AutoMirrored.Filled.CallReceived
                    CallLog.Calls.OUTGOING_TYPE -> Icons.AutoMirrored.Filled.CallMade
                    CallLog.Calls.BLOCKED_TYPE -> Icons.Filled.Block
                    CallLog.Calls.REJECTED_TYPE -> Icons.Outlined.Cancel
                    CallLog.Calls.VOICEMAIL_TYPE -> ImageVector.vectorResource(R.drawable.baseline_voicemail_24)
                    else -> Icons.Filled.SettingsPhone
                },
                contentDescription = when (callInformation.callType) {
                    CallLog.Calls.MISSED_TYPE -> stringResource(R.string.missed_call)
                    CallLog.Calls.INCOMING_TYPE -> stringResource(R.string.incoming_call)
                    CallLog.Calls.OUTGOING_TYPE -> stringResource(R.string.outgoing_call)
                    CallLog.Calls.BLOCKED_TYPE -> stringResource(R.string.blocked_call)
                    CallLog.Calls.REJECTED_TYPE -> stringResource(R.string.rejected_call)
                    CallLog.Calls.VOICEMAIL_TYPE -> stringResource(R.string.voicemail)
                    else -> stringResource(R.string.call)
                },
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        },
        headlineContent = {
            Text(callInformation.displayName)
        },
        supportingContent = {
            Text(convertLongToTimestamp(callInformation.callTimeStamp))
        },
        trailingContent = {
            if (callInformation.isFiltered()) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = when (callInformation.filterVerdict) {
                            FilterVerdict.BLOCKED ->
                                stringResource(R.string.blocked).uppercase()

                            FilterVerdict.SILENCED ->
                                stringResource(R.string.silenced).uppercase()

                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(
            headlineColor = if (callInformation.isFiltered()) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onBackground
            },
        )
    )
}

@Preview
@Composable
private fun ContentPreview1() {
    Content(
        state = {
            CallLogsState(
                isCallLogPermissionGranted = true,
                isLoadingCallLogs = false,
                callInformationList = listOf(
                    CallInformation(
                        displayName = "+453452",
                        callTimeStamp = 7578455503452,
                        callType = CallLog.Calls.OUTGOING_TYPE,
                        filterVerdict = FilterVerdict.UNFILTERED,
                    ),
                    CallInformation(
                        displayName = "+17298503495",
                        callTimeStamp = 7573948503452,
                        callType = CallLog.Calls.INCOMING_TYPE,
                        filterVerdict = FilterVerdict.SILENCED,
                    ),
                    CallInformation(
                        displayName = "+17298503495",
                        callTimeStamp = 7573948503452,
                        callType = CallLog.Calls.VOICEMAIL_TYPE,
                        filterVerdict = FilterVerdict.UNFILTERED,
                    ),
                ),
            )
        },
        uiScope = null,
        performAction = {}
    )
}

@Preview
@Composable
private fun ContentPreview2() {
    Content(
        state = {
            CallLogsState(
                isCallLogPermissionGranted = false,
                isLoadingCallLogs = false,
                callInformationList = listOf(),
            )
        },
        uiScope = null,
        performAction = {}
    )
}