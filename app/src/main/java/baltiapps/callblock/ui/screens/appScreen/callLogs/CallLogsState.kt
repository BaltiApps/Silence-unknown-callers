package baltiapps.callblock.ui.screens.appScreen.callLogs

import baltiapps.callblock.domain.models.CallInformation

data class CallLogsState(
    val isCallLogPermissionGranted: Boolean,
    val isLoadingCallLogs: Boolean,
    val callInformationList: List<CallInformation>,
)
