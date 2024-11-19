package baltiapps.callblock.domain.sources

import baltiapps.callblock.domain.models.CallInformation

interface PlatformCallLogSource {
    fun isCallLogPermissionGranted(): Boolean
    suspend fun getCallLogsList(): List<CallInformation>
}