package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.UIScope
import baltiapps.callblock.domain.sources.PlatformNotificationsApi
import baltiapps.callblock.domain.sources.PlatformPreferences

class ToggleNotificationsUseCase(
    private val notificationApi: PlatformNotificationsApi,
    private val preferences: PlatformPreferences,
) {

    private val currentState: Boolean
        get() = preferences.isNotificationsEnabled()

    operator fun invoke(
        uiScope: UIScope,
        callback: (isGranted: Boolean) -> Unit,
    ) {
        if (currentState) {
            preferences.setNotificationsEnabled(false)
        } else if (notificationApi.checkNotificationsPermission()) {
            preferences.setNotificationsEnabled(true)
            notificationApi.initNotifications()
        } else {
            uiScope.requestNotificationsPermission { isGranted ->
                preferences.setNotificationsEnabled(isGranted)
                notificationApi.initNotifications()
                callback(isGranted)
            }
            return
        }

        callback(currentState)
    }
}