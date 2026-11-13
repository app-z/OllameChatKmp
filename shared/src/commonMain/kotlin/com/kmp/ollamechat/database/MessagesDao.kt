package com.kmp.ollamechat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Query("SELECT * FROM Messages ORDER BY id")
    fun getAllMessages(): Flow<List<MessagesEntity>>

    @Query("SELECT * FROM Messages WHERE content LIKE '%' || :filter || '%' ORDER BY id")
    fun getFilteredMessages(filter: String): Flow<List<MessagesEntity>>

    @Query("SELECT * FROM Messages WHERE id = :id")
    suspend fun getMessages(id: String): MessagesEntity

    @Query("SELECT COUNT(*) as count FROM Messages")
    suspend fun count(): Int

    @Query("SELECT id FROM Messages ORDER BY id DESC")
    suspend fun getMaxId(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMessages(messages: List<MessagesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertMessage(message: MessagesEntity) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateMessage(message: MessagesEntity)

    @Delete
    suspend fun deleteMessages(message: MessagesEntity)

}
