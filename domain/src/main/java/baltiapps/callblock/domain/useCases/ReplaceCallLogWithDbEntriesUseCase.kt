package baltiapps.callblock.domain.useCases

import baltiapps.callblock.domain.models.CallInformation

class ReplaceCallLogWithDbEntriesUseCase {
    operator fun invoke(
        callLogs: List<CallInformation>,
        logsFromDb: List<CallInformation>,
    ): List<CallInformation> {
        val mutableDbList = logsFromDb.toMutableList()
        val replacedList = mutableListOf<CallInformation>()

        callLogs.forEach { logItem ->
            val dbItem = mutableDbList.find { dbLogItem ->
                logItem.displayName == dbLogItem.displayName
                        && logItem.callTimeStamp == dbLogItem.callTimeStamp
            }
            if (dbItem != null) {
                replacedList.add(
                    dbItem.copy(
                        displayName = logItem.displayName,
                        callType = logItem.callType,
                    )
                )
                mutableDbList.remove(dbItem)
            } else {
                replacedList.add(logItem)
            }
        }

        if (mutableDbList.isNotEmpty()) {
            replacedList.addAll(mutableDbList)
        }

        return replacedList.sortedByDescending { it.callTimeStamp }
    }
}