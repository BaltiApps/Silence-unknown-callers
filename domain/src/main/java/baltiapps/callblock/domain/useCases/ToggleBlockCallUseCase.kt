package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.UIScope
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformPreferences

class ToggleBlockCallUseCase(
    private val callFilterApi: PlatformCallFilterApi<*, *>,
    private val preferences: PlatformPreferences,
) {

    private val currentState: Boolean
        get() = preferences.isBlockEnabled()

    operator fun invoke(
        uiScope: UIScope,
        callback: (isGranted: Boolean) -> Unit,
    ) {

        if (currentState) {
            preferences.setBlockEnabled(false)
        } else if (callFilterApi.checkCallFilterPermission()) {
            preferences.setBlockEnabled(true)
        } else {
            uiScope.requestCallFilterPermission { isGranted ->
                preferences.setBlockEnabled(isGranted)
                callback(isGranted)
            }
            return
        }

        callback(currentState)
    }
}