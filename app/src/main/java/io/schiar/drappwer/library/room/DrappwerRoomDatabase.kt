package io.schiar.drappwer.library.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AppEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DrappwerRoomDatabase: RoomDatabase() {
    abstract fun appDAO(): AppDAO

    companion object {
        @Volatile
        private var Instance: DrappwerRoomDatabase? = null

        fun getDatabase(context: Context): DrappwerRoomDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = DrappwerRoomDatabase::class.java,
                    name = "drappwer_database"
                ).fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}