package baltiapps.callblock.data.sources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: FilterEntity)

    @Query("DELETE FROM FilterTable")
    suspend fun deleteAllRecords()

    @Query("SELECT * FROM FilterTable")
    fun getAllRecords(): Flow<List<FilterEntity>>
}