package com.example.android.politicalpreparedness.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.network.models.Election

@Database(entities = [Election::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase : RoomDatabase() {
    abstract fun electionDao(): ElectionDao

    companion object {
        fun createElectionDao(context: Context): ElectionDao {
            return Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java,
                "election_database"
            )
                .fallbackToDestructiveMigration()
                .build().electionDao()
        }
    }

}