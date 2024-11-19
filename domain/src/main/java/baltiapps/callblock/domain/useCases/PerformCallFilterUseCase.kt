package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.IncomingCallReceiver
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.models.FilterVerdict
import baltiapps.callblock.domain.models.PlatformCallData
import baltiapps.callblock.domain.sources.PlatformCallFilterApi

class PerformCallFilterUseCase(
    private val callFilterApi: PlatformCallFilterApi<PlatformCallData, IncomingCallReceiver>,
) {
    operator fun invoke(
        receiver: IncomingCallReceiver,
        callData: PlatformCallData,
        callInformation: CallInformation,
    ) {
        when (callInformation.filterVerdict) {
            FilterVerdict.BLOCKED -> callFilterApi.blockCall(receiver, callData)
            FilterVerdict.SILENCED -> callFilterApi.silenceCall(receiver, callData)
            else -> {}
        }
    }
}