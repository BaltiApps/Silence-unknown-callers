package baltiapps.callblock.data.sources

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.CallLog
import baltiapps.callblock.domain.models.CallInformation
import baltiapps.callblock.domain.sources.PlatformCallLogSource

class CallLogSource(
    private val context: Context,
): PlatformCallLogSource {
    override fun isCallLogPermissionGranted(): Boolean {
        return context.checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun getCallLogsList(): List<CallInformation> {
        if (!isCallLogPermissionGranted()) return emptyList()

        val callList = mutableListOf<CallInformation>()

        val cursor = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            null
        ) ?: throw Exception("Call log read cursor is null!")

        val totalCount = cursor.count
        if (totalCount != 0) {
            cursor.moveToFirst()
        }

        cursor.use {
            for (i in 0 until totalCount) {
                getSingleCallLog(it).run {
                    callList.add(this)
                    it.moveToNext()
                }
            }
        }

        return callList.sortedByDescending { it.callTimeStamp }
    }

    private fun getSingleCallLog(
        cursor: Cursor,
    ): CallInformation {
        val callerNumber = getCursorData<String>(cursor, CallLog.Calls.NUMBER)
        val callerName = getCursorData<String>(cursor, CallLog.Calls.CACHED_NAME)
        val timeStamp = getCursorData<Long>(cursor, CallLog.Calls.DATE)
        val callDirection = getCursorData<Int>(cursor, CallLog.Calls.TYPE)

        return CallInformation(
            displayName = callerName.takeIf { it.isNotBlank() } ?: callerNumber,
            callTimeStamp = timeStamp,
            callType = callDirection,
        )
    }

    private inline fun <reified T> getCursorData(
        cursor: Cursor,
        columnName: String,
    ): T {
        val columnIndex = cursor.getColumnIndex(columnName)
        if (columnIndex < 0)
            throw Exception("Column $columnName index less than 0 - $columnIndex")
        return when(T::class) {
            String::class -> (cursor.getString(columnIndex) ?: "") as T
            Int::class -> cursor.getInt(columnIndex) as T
            Long::class -> cursor.getLong(columnIndex) as T
            Boolean::class -> (cursor.getInt(columnIndex) > 0) as T
            else -> throw IllegalStateException("Cursor read - Unknown data type - ${T::class}")
        }
    }
}