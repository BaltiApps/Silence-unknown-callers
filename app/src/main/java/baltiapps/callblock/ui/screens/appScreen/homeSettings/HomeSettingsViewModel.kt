package baltiapps.callblock.ui.screens.appScreen.homeSettings

import androidx.lifecycle.ViewModel
import baltiapps.callblock.data.ScreeningService
import baltiapps.callblock.data.models.CallData
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformNotificationsApi
import baltiapps.callblock.domain.sources.PlatformPreferences
import baltiapps.callblock.domain.useCases.ToggleBlockCallUseCase
import baltiapps.callblock.domain.useCases.ToggleNotificationsUseCase
import baltiapps.callblock.domain.useCases.ToggleSilenceCallUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeSettingsViewModel(
    private val toggleSilenceCallUseCase: ToggleSilenceCallUseCase,
    private val toggleBlockCallUseCase: ToggleBlockCallUseCase,
    private val toggleNotificationsUseCase: ToggleNotificationsUseCase,
    private val preferences: PlatformPreferences,
    private val callFilterApi: PlatformCallFilterApi<CallData, ScreeningService>,
    private val notificationsApi: PlatformNotificationsApi,
): ViewModel() {

    private val _state: MutableStateFlow<HomeSettingsState> = MutableStateFlow(
        getState()
    )
    val state = _state.asStateFlow()

    fun performAction(action: HomeSettingsAction) {
        when (action) {
            is HomeSettingsAction.ToggleSilenceCalls ->
                toggleSilenceCallUseCase.invoke(action.activity) { isEnabled ->
                    _state.update {
                        it.copy(isSilenceEnabled = isEnabled)
                    }
                }

            is HomeSettingsAction.ToggleBlockCalls ->
                toggleBlockCallUseCase.invoke(action.activity) { isEnabled ->
                    _state.update {
                        it.copy(isBlockEnabled = isEnabled)
                    }
                }

            is HomeSettingsAction.ToggleNotifications ->
                toggleNotificationsUseCase.invoke(action.activity) { isEnabled ->
                    _state.update {
                        it.copy(isNotificationsEnabled = isEnabled)
                    }
                }

            else -> {}
        }
    }

    private fun getState(): HomeSettingsState {
        return HomeSettingsState(
            isSilenceEnabled = callFilterApi.checkCallFilterPermission()
                    && preferences.isSilenceEnabled(),
            isBlockEnabled = callFilterApi.checkCallFilterPermission()
                    && preferences.isBlockEnabled(),
            isNotificationsEnabled = notificationsApi.checkNotificationsPermission()
                    && preferences.isNotificationsEnabled(),
            isSystemNotificationsEnabled = preferences.isSystemNotificationsEnabled()
        )
    }
}