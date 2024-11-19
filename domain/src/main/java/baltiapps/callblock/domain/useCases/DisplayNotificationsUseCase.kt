package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.models.FilterVerdict
import baltiapps.callblock.domain.sources.PlatformNotificationsApi
import baltiapps.callblock.domain.sources.PlatformPreferences

class DisplayNotificationsUseCase(
    private val notificationsApi: PlatformNotificationsApi,
    private val preferences: PlatformPreferences,
) {
    operator fun invoke(callInformation: CallInformation) {
        when {
            !preferences.isNotificationsEnabled() -> {}
            !notificationsApi.checkNotificationsPermission() -> {}
            callInformation.filterVerdict == FilterVerdict.BLOCKED -> {
                notificationsApi.showBlockedNotifications(callInformation)
            }
            callInformation.filterVerdict == FilterVerdict.SILENCED -> {
                notificationsApi.showSilencedNotifications(callInformation)
            }
        }
    }
}