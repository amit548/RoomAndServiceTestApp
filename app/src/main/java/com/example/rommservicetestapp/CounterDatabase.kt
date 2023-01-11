package com.example.rommservicetestapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Counter::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {

    abstract fun counterDao(): CounterDao
}