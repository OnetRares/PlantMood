package com.example.plantmood.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: UsageEntity)

    @Query("""
        SELECT * FROM usage_table 
        ORDER BY date DESC 
        LIMIT 7
    """)
    suspend fun getLast7Days(): List<UsageEntity>
}