package com.example.myapplication.BDD

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllPlaces(): List<Locations>

}

@Dao
interface BoatDao {
    @Query("SELECT * FROM boats")
    fun getAllBoats(): List<Boat>
}

@Dao
interface LevelDao{

    @Query("select * from levels")
    fun getAllLevels():List<Level>

}