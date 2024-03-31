package com.example.myapplication.bdd

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM Locations")
    fun getAllPlaces(): List<Locations>

}

@Dao
interface BoatDao {
    @Query("SELECT * FROM Boats")
    fun getAllBoats(): List<Boats>
}

@Dao
interface LevelDao{

    @Query("select * from Levels")
    fun getAllLevels():List<Levels>

}