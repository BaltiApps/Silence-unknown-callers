package baltiapps.callblock.domain.repository

import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.sources.PlatformDBSource
import kotlinx.coroutines.flow.Flow

class Repository(
    private val dbSource: PlatformDBSource,
) {
    fun getFilteredCallsHistory(): Flow<List<CallInformation>> {
        return dbSource.getFilteredCallsHistory()
    }

    suspend fun clearFilteredCallsHistory() {
        dbSource.clearFilteredCallsHistory()
    }

    suspend fun addFilteredCall(callInformation: CallInformation) {
        if (callInformation.isFiltered()) {
            dbSource.addFilteredCall(callInformation)
        }
    }
}