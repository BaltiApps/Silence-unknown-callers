package baltiapps.callblock.data

import android.telecom.Call
import android.telecom.CallScreeningService
import baltiapps.callblock.data.models.CallData
import baltiapps.callblock.domain.IncomingCallReceiver
import baltiapps.callblock.domain.repository.Repository
import baltiapps.callblock.domain.useCases.DisplayNotificationsUseCase
import baltiapps.callblock.domain.useCases.PerformCallFilterUseCase
import baltiapps.callblock.domain.useCases.ShouldBlockCallUseCase
import baltiapps.callblock.domain.useCases.ShouldSilenceCallUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ScreeningService: CallScreeningService(), IncomingCallReceiver {

    override val shouldBlockCallUseCase: ShouldBlockCallUseCase by inject()
    override val shouldSilenceCallUseCase: ShouldSilenceCallUseCase by inject()
    override val displayNotificationsUseCase: DisplayNotificationsUseCase by inject()
    override val performCallFilterUseCase: PerformCallFilterUseCase by inject()

    private val repository: Repository by inject()

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onScreenCall(callDetails: Call.Details) {
        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        if (isIncoming) {
            val callData = CallData(
                callDetails = callDetails
            )
            val callInformation = onCallReceived(callData)
            scope.launch {
                repository.addFilteredCall(callInformation)
            }
        }
    }
}