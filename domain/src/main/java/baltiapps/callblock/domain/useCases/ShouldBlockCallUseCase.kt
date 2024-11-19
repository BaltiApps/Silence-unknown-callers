package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.models.PlatformCallData
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformPreferences

class ShouldBlockCallUseCase(
    private val callFilterApi: PlatformCallFilterApi<PlatformCallData, *>,
    private val preferences: PlatformPreferences,
) {
    operator fun invoke(callData: PlatformCallData): Boolean {
        return when {
            !callFilterApi.checkCallFilterPermission() -> false
            !preferences.isBlockEnabled() -> false
            callFilterApi.isUnknownCaller(callData) -> true
            else -> true
        }
    }
}