package com.example.myapplication.bdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Locations::class, Boats::class, Levels::class], version = MyAppDatabase.DATABASE_VERSION)
abstract class MyAppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun boatDao(): BoatDao
    abstract fun levelDao(): LevelDao
    companion object {
        const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "gestion_plongees"

        @Volatile
        private var INSTANCE: MyAppDatabase? = null

        fun getDatabase(context: Context): MyAppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyAppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
