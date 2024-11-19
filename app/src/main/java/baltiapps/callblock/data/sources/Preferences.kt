package baltiapps.callblock.data.sources

import android.content.Context
import androidx.preference.PreferenceManager
import baltiapps.callblock.domain.sources.PlatformPreferences

class Preferences(
    private val context: Context,
): PlatformPreferences {

    private val sharedPrefs by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val editor by lazy {
        sharedPrefs.edit()
    }

    companion object {
        private const val IS_FIRST_SETUP_DONE = "is_first_setup_done"
        private const val IS_SILENCE_ENABLED = "is_silence_enabled"
        private const val IS_BLOCK_ENABLED = "is_block_enabled"
        private const val IS_NOTIFICATIONS_ENABLED = "is_notifications_enabled"
        private const val IS_SYSTEM_NOTIFICATIONS_ENABLED = "is_system_notifications_enabled"
    }

    override fun isFirstSetupDone(): Boolean {
        return sharedPrefs.getBoolean(IS_FIRST_SETUP_DONE, false)
    }

    override fun completeFirstSetup() {
        editor.putBoolean(IS_FIRST_SETUP_DONE, true).apply()
    }

    override fun isSilenceEnabled(): Boolean {
        return sharedPrefs.getBoolean(IS_SILENCE_ENABLED, false)
    }

    override fun isBlockEnabled(): Boolean {
        return sharedPrefs.getBoolean(IS_BLOCK_ENABLED, false)
    }

    override fun isNotificationsEnabled(): Boolean {
        return sharedPrefs.getBoolean(IS_NOTIFICATIONS_ENABLED, false)
    }

    override fun isSystemNotificationsEnabled(): Boolean {
        return sharedPrefs.getBoolean(IS_SYSTEM_NOTIFICATIONS_ENABLED, false)
    }

    override fun setSilenceEnabled(enabled: Boolean) {
        editor.apply {
            putBoolean(IS_SILENCE_ENABLED, enabled)
            apply()
        }
    }

    override fun setBlockEnabled(enabled: Boolean) {
        editor.apply {
            putBoolean(IS_BLOCK_ENABLED, enabled)
            apply()
        }
    }

    override fun setNotificationsEnabled(enabled: Boolean) {
        editor.apply {
            putBoolean(IS_NOTIFICATIONS_ENABLED, enabled)
            apply()
        }
    }

    override fun setSystemNotificationsEnabled(enabled: Boolean) {
        editor.apply {
            putBoolean(IS_SYSTEM_NOTIFICATIONS_ENABLED, enabled)
            apply()
        }
    }
}