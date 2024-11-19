package baltiapps.callblock.ui.screens.appScreen

import androidx.lifecycle.ViewModel
import baltiapps.callblock.domain.sources.PlatformPreferences

class AppScreenViewModel(
    private val preferences: PlatformPreferences,
): ViewModel() {
    fun isFirstSetupDone(): Boolean {
        return preferences.isFirstSetupDone()
    }
}