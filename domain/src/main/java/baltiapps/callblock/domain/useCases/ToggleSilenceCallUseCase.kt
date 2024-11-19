package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.UIScope
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformPreferences

class ToggleSilenceCallUseCase(
    private val callFilterApi: PlatformCallFilterApi<*, *>,
    private val preferences: PlatformPreferences,
) {

    private val currentState: Boolean
        get() = preferences.isSilenceEnabled()

    operator fun invoke(
        uiScope: UIScope,
        callback: (isGranted: Boolean) -> Unit,
    ) {

        if (currentState) {
            preferences.setSilenceEnabled(false)
        } else if (callFilterApi.checkCallFilterPermission()) {
            preferences.setSilenceEnabled(true)
        } else {
            uiScope.requestCallFilterPermission { isGranted ->
                preferences.setSilenceEnabled(isGranted)
                callback(isGranted)
            }
            return
        }

        callback(currentState)
    }
}