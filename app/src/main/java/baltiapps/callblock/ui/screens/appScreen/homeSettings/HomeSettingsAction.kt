package baltiapps.callblock.ui.screens.appScreen.homeSettings

import baltiapps.callblock.domain.UIScope

sealed class HomeSettingsAction {
    class ToggleSilenceCalls(val activity: UIScope): HomeSettingsAction()
    class ToggleBlockCalls(val activity: UIScope): HomeSettingsAction()
    class ToggleNotifications(val activity: UIScope): HomeSettingsAction()
}