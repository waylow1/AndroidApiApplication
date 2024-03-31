package com.example.myapplication.bdd

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query



@Dao
interface LevelDao{

    @Query("select * from Levels")
    suspend fun getAllLevels():List<Levels>

    @Insert
    suspend fun insert(level: Levels)

}