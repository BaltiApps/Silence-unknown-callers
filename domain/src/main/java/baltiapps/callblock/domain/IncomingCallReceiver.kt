package baltiapps.callblock.domain

import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.models.FilterVerdict
import baltiapps.callblock.domain.models.PlatformCallData
import baltiapps.callblock.domain.useCases.DisplayNotificationsUseCase
import baltiapps.callblock.domain.useCases.PerformCallFilterUseCase
import baltiapps.callblock.domain.useCases.ShouldBlockCallUseCase
import baltiapps.callblock.domain.useCases.ShouldSilenceCallUseCase

interface IncomingCallReceiver {

    val shouldBlockCallUseCase: ShouldBlockCallUseCase
    val shouldSilenceCallUseCase: ShouldSilenceCallUseCase
    val displayNotificationsUseCase: DisplayNotificationsUseCase
    val performCallFilterUseCase: PerformCallFilterUseCase

    fun onCallReceived(callData: PlatformCallData): CallInformation {
        val callInformation: CallInformation = callData.toCallInformation().run {
            when {
                shouldBlockCallUseCase.invoke(callData) -> {
                    this.copy(filterVerdict = FilterVerdict.BLOCKED)
                }
                shouldSilenceCallUseCase.invoke(callData) -> {
                    this.copy(filterVerdict = FilterVerdict.SILENCED)
                }
                else -> this
            }
        }

        performCallFilterUseCase.invoke(
            receiver = this,
            callData = callData,
            callInformation = callInformation,
        )
        displayNotificationsUseCase(callInformation)

        return callInformation
    }
}