package baltiapps.callblock.domain.sources

import baltiapps.callblock.domain.models.CallInformation
import kotlinx.coroutines.flow.Flow

interface PlatformDBSource {
    fun getFilteredCallsHistory(): Flow<List<CallInformation>>
    suspend fun clearFilteredCallsHistory()
    suspend fun addFilteredCall(callInformation: CallInformation)
}