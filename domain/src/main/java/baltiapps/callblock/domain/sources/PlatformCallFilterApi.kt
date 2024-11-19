package baltiapps.callblock.domain.sources

import baltiapps.callblock.domain.IncomingCallReceiver
import baltiapps.callblock.domain.models.PlatformCallData

interface PlatformCallFilterApi<T : PlatformCallData, V : IncomingCallReceiver> {
    fun checkCallFilterPermission(): Boolean
    fun isUnknownCaller(callData: T): Boolean
    fun blockCall(receiver: V, callData: T)
    fun silenceCall(receiver: V, callData: T)
}