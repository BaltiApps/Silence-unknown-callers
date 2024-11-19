package baltiapps.callblock.ui.screens.setup

import androidx.lifecycle.ViewModel
import baltiapps.callblock.data.ScreeningService
import baltiapps.callblock.data.models.CallData
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformCallLogSource
import baltiapps.callblock.domain.sources.PlatformPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FirstSetupViewModel(
    private val preferences: PlatformPreferences,
    private val callFilterApi: PlatformCallFilterApi<CallData, ScreeningService>,
    private val callLogSource: PlatformCallLogSource,
): ViewModel() {

    private val _state: MutableStateFlow<FirstSetupState> = MutableStateFlow(
        FirstSetupState(
            isCallFilterPermissionGranted = callFilterApi.checkCallFilterPermission(),
            isCallLogPermissionGranted = callLogSource.isCallLogPermissionGranted(),
        )
    )
    val state = _state.asStateFlow()

    fun performAction(action: FirstSetupAction) {
        when(action) {
            is FirstSetupAction.RequestCallFilterPermission ->
                action.uiScope.requestCallFilterPermission { isGranted ->
                    _state.update {
                        it.copy(
                            isCallFilterPermissionGranted = isGranted
                        )
                    }
                }

            is FirstSetupAction.RequestCallLogPermission ->
                action.uiScope.requestCallLogPermission { isGranted ->
                    _state.update {
                        it.copy(
                            isCallLogPermissionGranted = isGranted
                        )
                    }
                }

            FirstSetupAction.FinishFirstStep -> {
                preferences.completeFirstSetup()
            }
        }
    }

}