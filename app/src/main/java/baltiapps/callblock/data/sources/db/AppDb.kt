package baltiapps.callblock.data.sources.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FilterEntity::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun filterDataDao(): FilterDataDao
}