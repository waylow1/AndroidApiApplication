package com.example.myapplication.BDD

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class Locations(
    @PrimaryKey val id: Int,
    val name: String,
)

@Entity(tableName = "boats")
data class Boat(
    @PrimaryKey val id: Int,
    val name: String,
)