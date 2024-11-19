package baltiapps.callblock.domain.sources

import baltiapps.callblock.domain.models.CallInformation

interface PlatformNotificationsApi {
    fun checkNotificationsPermission(): Boolean
    fun showSilencedNotifications(callInformation: CallInformation)
    fun showBlockedNotifications(callInformation: CallInformation)
    fun initNotifications()
}