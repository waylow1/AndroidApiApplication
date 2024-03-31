package com.example.myapplication.bdd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities =  [Levels::class], version = MyAppDatabase.DATABASE_VERSION)
abstract class MyAppDatabase : RoomDatabase() {
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
