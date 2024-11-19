package baltiapps.callblock.domain.sources

interface PlatformPreferences {
    fun isFirstSetupDone(): Boolean
    fun completeFirstSetup()
    fun isSilenceEnabled(): Boolean
    fun isBlockEnabled(): Boolean
    fun isNotificationsEnabled(): Boolean
    fun isSystemNotificationsEnabled(): Boolean
    fun setSilenceEnabled(enabled: Boolean)
    fun setBlockEnabled(enabled: Boolean)
    fun setNotificationsEnabled(enabled: Boolean)
    fun setSystemNotificationsEnabled(enabled: Boolean)
}