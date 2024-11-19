package baltiapps.callblock.data.sources.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FilterTable")
data class FilterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val displayName: String,
    val callTimeStamp: Long,
    val filterVerdict: String,
)