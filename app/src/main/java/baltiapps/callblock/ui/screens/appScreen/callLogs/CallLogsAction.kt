package baltiapps.callblock.ui.screens.appScreen.callLogs

import baltiapps.callblock.domain.UIScope

sealed class CallLogsAction {
    class RequestCallLogPermission(val uiScope: UIScope): CallLogsAction()
}