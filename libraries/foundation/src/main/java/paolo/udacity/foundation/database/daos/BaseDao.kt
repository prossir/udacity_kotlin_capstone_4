package paolo.udacity.foundation.database.daos

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update


interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(vararg obj: T)

    @Insert
    suspend fun delete(vararg obj: T)

}