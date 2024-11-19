package baltiapps.callblock.data.models

import android.provider.CallLog
import android.telecom.Call
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.models.PlatformCallData
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar

data class CallData(
    val callDetails: Call.Details
): PlatformCallData {
    override fun toCallInformation(): CallInformation {
        val phoneNumber = callDetails.handle?.schemeSpecificPart ?: ""
        return CallInformation(
            displayName = phoneNumber,
            callTimeStamp = callDetails.creationTimeMillis,
            callType = CallLog.Calls.INCOMING_TYPE,
        )
    }
}
