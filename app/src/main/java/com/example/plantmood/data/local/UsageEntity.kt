package com.example.plantmood.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_table")
data class UsageEntity(
    @PrimaryKey val date: Long,
    val totalScreenTimeHours: Double,
    val socialMediaHours: Double,
    val usedAfterMidnight: Boolean
)