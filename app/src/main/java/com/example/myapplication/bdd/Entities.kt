package com.example.myapplication.bdd

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="Levels")
data class Levels(
    @PrimaryKey val id: Int,
    val name:String
)