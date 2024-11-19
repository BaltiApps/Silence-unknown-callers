package baltiapps.callblock.data.sources.db

import android.provider.CallLog
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.models.FilterVerdict
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun FilterEntity.toCallInformation(): CallInformation {
    return CallInformation(
        displayName = displayName,
        callTimeStamp = callTimeStamp,
        callType = CallLog.Calls.INCOMING_TYPE,
        filterVerdict = FilterVerdict.valueOf(filterVerdict)
    )
}

fun CallInformation.toEntity(): FilterEntity {
    return FilterEntity(
        id = 0,
        displayName = displayName,
        callTimeStamp = callTimeStamp,
        filterVerdict = filterVerdict.name
    )
}

fun List<FilterEntity>.toCallInformation(): List<CallInformation> {
    return map { it.toCallInformation() }
}

fun Flow<List<FilterEntity>>.toCallInformation(): Flow<List<CallInformation>> {
    return map { it.toCallInformation() }
}