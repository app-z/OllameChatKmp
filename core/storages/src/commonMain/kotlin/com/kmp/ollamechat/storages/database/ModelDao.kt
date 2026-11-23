package com.kmp.ollamechat.storages.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ModelDao {

    @Query("SELECT * FROM models")
    suspend fun getModels() : List<ModelsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<ModelsEntity>)

    @Query("SELECT * FROM models")
    fun observeModelList() : Flow<List<ModelsEntity>>

    @Upsert
    suspend fun replaceAll(models: List<ModelsEntity>)

    @Query("UPDATE models SET is_selected = 0")
    suspend fun clearSelection()

    @Query("UPDATE models SET is_selected = 1 WHERE name = :name")
    suspend fun selectByName(name: String)

    @Transaction
    suspend fun selectExclusive(name: String) {
        clearSelection()
        selectByName(name)
    }
}