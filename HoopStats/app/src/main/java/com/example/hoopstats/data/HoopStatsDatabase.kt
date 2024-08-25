package com.example.hoopstats.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Drill::class, DrillSession::class], version = 5)
@TypeConverters(DateTypeConverter::class)
abstract class HoopStatsDatabase : RoomDatabase() {

    abstract fun getDrillDao(): DrillDao

    abstract fun getDrillSessionDao(): DrillSessionDao

    companion object {

        @Volatile
        private var DATABASE_INSTANCE: HoopStatsDatabase? = null

        fun getDatabase(context: Context): HoopStatsDatabase {

            return DATABASE_INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    HoopStatsDatabase::class.java,
                    "HoopStats-database"
                )
                    .fallbackToDestructiveMigration() // usunąć na koniec pracy
                    .build()
                DATABASE_INSTANCE = instance
                instance
            }
        }
    }
}


