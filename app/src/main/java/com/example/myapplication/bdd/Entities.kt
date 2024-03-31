package com.example.myapplication.bdd

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Locations")
data class Locations(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName = "Boats")
data class Boats(
    @PrimaryKey val id: Int,
    val name: String
)

@Entity(tableName="Levels")
data class Levels(
    @PrimaryKey val id: Int,
    val name:String
)