package baltiapps.callblock.data.sources

import baltiapps.callblock.data.sources.db.FilterDataDao
import baltiapps.callblock.data.sources.db.toCallInformation
import baltiapps.callblock.data.sources.db.toEntity
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.sources.PlatformDBSource
import kotlinx.coroutines.flow.Flow

class DBSource(
    private val filterDataDao: FilterDataDao,
): PlatformDBSource {
    override fun getFilteredCallsHistory(): Flow<List<CallInformation>> {
        return filterDataDao.getAllRecords().toCallInformation()
    }

    override suspend fun clearFilteredCallsHistory() {
        filterDataDao.deleteAllRecords()
    }

    override suspend fun addFilteredCall(callInformation: CallInformation) {
        filterDataDao.insertRecord(callInformation.toEntity())
    }
}