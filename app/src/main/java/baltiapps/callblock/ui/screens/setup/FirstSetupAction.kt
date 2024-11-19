package baltiapps.callblock.ui.screens.setup

import baltiapps.callblock.domain.UIScope

sealed class FirstSetupAction {
    class RequestCallFilterPermission(val uiScope: UIScope): FirstSetupAction()
    class RequestCallLogPermission(val uiScope: UIScope): FirstSetupAction()
    data object FinishFirstStep : FirstSetupAction()
}