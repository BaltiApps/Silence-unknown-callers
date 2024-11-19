package baltiapps.callblock.data.sources

import android.app.role.RoleManager
import android.content.Context
import android.telecom.CallScreeningService.CallResponse
import baltiapps.callblock.data.ScreeningService
import baltiapps.callblock.data.models.CallData
import baltiapps.callblock.domain.sources.PlatformCallFilterApi
import baltiapps.callblock.domain.sources.PlatformPreferences

class CallFilterApi(
    private val context: Context,
    private val preferences: PlatformPreferences,
): PlatformCallFilterApi<CallData, ScreeningService> {

    private val roleManager by lazy {
        context.getSystemService(RoleManager::class.java)
    }

    override fun checkCallFilterPermission(): Boolean {
        return roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)
    }

    // Android only triggers CallScreeningService for callers not saved in contacts
    override fun isUnknownCaller(callData: CallData): Boolean {
        return true
    }

    override fun blockCall(
        receiver: ScreeningService,
        callData: CallData,
    ) {
        val response = CallResponse.Builder()
            .setDisallowCall(true)
            .setRejectCall(true)
            .setSkipNotification(preferences.isSystemNotificationsEnabled())
            .build()
        receiver.respondToCall(callData.callDetails, response)
    }

    override fun silenceCall(
        receiver: ScreeningService,
        callData: CallData,
    ) {
        val response = CallResponse.Builder()
            .setSilenceCall(true)
            .setSkipNotification(preferences.isSystemNotificationsEnabled())
            .build()
        receiver.respondToCall(callData.callDetails, response)
    }
}