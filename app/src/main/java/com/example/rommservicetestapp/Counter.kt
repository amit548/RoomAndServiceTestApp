package com.example.rommservicetestapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter")
data class Counter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val count: Long
)