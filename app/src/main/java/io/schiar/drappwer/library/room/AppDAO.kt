package io.schiar.drappwer.library.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(app: AppEntity): Long

    @Update
    suspend fun update(app: AppEntity)

    @Query("SELECT * FROM App ORDER BY name")
    fun select(): Flow<List<AppEntity>>

    @Query("SELECT * FROM App WHERE selected is 1 ORDER BY name")
    fun selectSelected(): Flow<List<AppEntity>>
}